package kind.sun.dev.coffeeworld.view.dialog.profile

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.databinding.FragmentAvatarBinding
import kind.sun.dev.coffeeworld.viewmodel.ProfileViewModel
import kind.sun.dev.coffeeworld.utils.api.NetworkResult
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.helper.storage.FileInternalHelper
import kind.sun.dev.coffeeworld.utils.custom.CustomLoadingDialog
import kind.sun.dev.coffeeworld.utils.helper.view.checkPermission
import kind.sun.dev.coffeeworld.utils.helper.view.checkSDKTiramisu
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class AvatarBottomFragment(
    private val onUpdateSuccess: () -> Unit
) : BottomSheetDialogFragment() {
    private var _binding: FragmentAvatarBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel>()
    @Inject lateinit var loadingDialog: CustomLoadingDialog

    private val fileInternalHelper by lazy { FileInternalHelper(requireContext()) }
    private lateinit var currentFileName: String
    private var bitmap: Bitmap? = null
    private var uri: Uri? = null

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var pickImageGalleryLauncher:  ActivityResultLauncher<Intent>
    private lateinit var takeImageCameraLauncher: ActivityResultLauncher<Intent>
    private var currentAction = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAvatarBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            this::onPermissionRequestResult
        )
        pickImageGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            this::onPickImageGalleryResult
        )
        takeImageCameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            this::onTakeImageCameraResult
        )

        setupUserUpdateLiveData()
    }

    private fun onPermissionRequestResult(isGranted: Boolean) {
        if (isGranted) {
            when (currentAction) {
                Constants.OPEN_GALLERY -> { openImagePicker() }
                Constants.OPEN_CAMERA -> { captureImageFromCamera() }
            }
        } else {
            val message: String
            when (currentAction) {
                Constants.OPEN_GALLERY -> {
                    message = "App needs photo and video permission for gallery access"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
                Constants.OPEN_CAMERA -> {
                    message = "App needs camera permission for take photo"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onPickImageGalleryResult(result: ActivityResult) {
        try {
            if (result.resultCode == FragmentActivity.RESULT_OK && result.data != null) {
                val selectedImageUri = result.data?.data
                selectedImageUri?.let {
                    this.uri = it
                    handleImageFromSource(uri = it)
                }
            }
        } catch (e: Exception) {
            Logger.error("Error getting selected files ${e.message}")
        }
    }

    private fun onTakeImageCameraResult(result: ActivityResult) {
        if (result.resultCode == FragmentActivity.RESULT_OK && result.data != null) {
            try {
                val bundle = result.data?.extras
                @Suppress("DEPRECATION") val bitmap = bundle?.get("data") as Bitmap
                this.bitmap = bitmap
                handleImageFromSource(bitmap = bitmap)
            } catch (e: Exception) {
                Logger.error("takeImageCameraLauncher: ${e.message}")
            }
        }
    }

    private fun handleImageFromSource(uri: Uri? = null, bitmap: Bitmap? = null) {
        if (uri != null && bitmap != null) {
            throw IllegalArgumentException("Both URI and Bitmap provided. Please provide either URI or Bitmap.")
        } else {
            lifecycleScope.launch {
                var avatarFile: File? = null
                if (uri != null) {
                    avatarFile = fileInternalHelper.savePhotoByUri(uri)
                } else if (bitmap != null) {
                    avatarFile = fileInternalHelper.savePhotoByBitmap(bitmap)
                }
                if (avatarFile?.exists() == true) {
                    currentFileName = avatarFile.name
                    profileViewModel.onUpdateAvatar(avatarFile)
                }
            }
        }
    }

    fun onClickOpenGallery() {
        currentAction = Constants.OPEN_GALLERY
        checkSDKTiramisu(
            onSDKTiramisu = {
                checkPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES,
                    onGranted = { openImagePicker() },
                    onDenied = {
                        requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                )
            },
            onNotTiramisu = {
                checkPermission(requireContext(),  Manifest.permission.READ_EXTERNAL_STORAGE,
                    onGranted = { openImagePicker() },
                    onDenied = {
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                )
            }
        )
    }

    private fun openImagePicker() {
        try {
            val pickImageIntentPicker = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
            }
            pickImageGalleryLauncher.launch(pickImageIntentPicker)
        } catch (e: Exception) {
            Logger.error("pickImageGalleryLauncher: ${e.message}")
        }
    }

    fun onClickOpenCamera() {
        currentAction =Constants.OPEN_CAMERA
        checkPermission(requireContext(), Manifest.permission.CAMERA,
            onGranted = { captureImageFromCamera() },
            onDenied = { requestPermissionLauncher.launch(Manifest.permission.CAMERA) }
        )
    }

    private fun captureImageFromCamera() {
        try {
            val takeImageIntentCapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takeImageCameraLauncher.launch(takeImageIntentCapture)
        } catch (e: Exception) {
            Logger.error("takeImageCameraLauncher: ${e.message}")
        }
    }

    private fun setupUserUpdateLiveData() {
        profileViewModel.messageResponse.observe(viewLifecycleOwner) { result ->
            when(result) {
                is NetworkResult.Success -> {
                    if (loadingDialog.isAdded) {
                        handleDeleteInternalFile(message = result.data!!.data)
                    }
                }
                is NetworkResult.Error -> {
                    if (loadingDialog.isAdded) {
                        loadingDialog.dismiss()
                        Logger.error(result.message.toString())
                    }
                }
                is NetworkResult.Loading -> {
                    loadingDialog.show(childFragmentManager, CustomLoadingDialog::class.simpleName)
                }
            }
        }
    }

    private fun handleDeleteInternalFile(message: String) {
        lifecycleScope.launch {
            val deleted = fileInternalHelper.deletePhoto(currentFileName)
            if (deleted) {
                loadingDialog.dismiss()
                onUpdateSuccess.invoke()
                onBackToProfileFragment()
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onBackToProfileFragment() {
        this.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}