package kind.sun.dev.coffeeworld.view.bsdf.user

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.base.BaseBSDF
import kind.sun.dev.coffeeworld.databinding.BsdfProfileUpdateAvatarBinding
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.helper.storage.FileInternalHelper
import kind.sun.dev.coffeeworld.utils.helper.view.checkPermission
import kind.sun.dev.coffeeworld.utils.helper.view.checkSDKTiramisu
import kind.sun.dev.coffeeworld.utils.helper.view.showToastError
import kind.sun.dev.coffeeworld.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileUpdateAvatarBSDF(
    private val onUpdateSuccess: (message: String) -> Unit
) : BaseBSDF<BsdfProfileUpdateAvatarBinding, UserViewModel>(false, BsdfProfileUpdateAvatarBinding::inflate) {

    private val fileInternalHelper by lazy { FileInternalHelper(requireContext()) }
    private lateinit var currentFileName: String
    private var actionListener: Int = 0
    private val openGallery = 1
    private val openCamera = 2

    override val viewModel: UserViewModel by viewModels()

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (actionListener == openGallery) openImagePicker()
            if (actionListener == openCamera) captureImageFromCamera()
        } else {
            val galleryMessage = "App needs photo and video permission for gallery access"
            val cameraMessage = "App needs camera permission for take photo"
            if (actionListener == openGallery)
                StyleableToast.makeText(requireContext(), galleryMessage, R.style.toast_permission).show()
            if (actionListener == openCamera)
                StyleableToast.makeText(requireContext(), cameraMessage, R.style.toast_permission).show()
        }
    }

    private val pickImageGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == FragmentActivity.RESULT_OK && result.data != null) {
            try {
                result.data?.data?.let { handleImageFromSource(uri = it) }
            } catch (e: Exception) {
                Logger.error(message = "[Avatar] takeImageCameraLauncher: ${e.message}")
            }
        }
    }

    private val takeImageCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == FragmentActivity.RESULT_OK && result.data != null) {
            try {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                handleImageFromSource(bitmap = bitmap)
            } catch (e: Exception) {
                Logger.error(message = "[Avatar] takeImageCameraLauncher: ${e.message}")
            }
        }
    }

    private fun handleImageFromSource(uri: Uri? = null, bitmap: Bitmap? = null) {
        if (uri != null && bitmap != null) {
            throw IllegalArgumentException("Both URI and Bitmap provided. Please provide either URI or Bitmap.")
        }
        lifecycleScope.launch {
            val avatarFile = if (uri != null) {
                fileInternalHelper.savePhotoByUri(uri)
            } else  {
                bitmap?.let { fileInternalHelper.savePhotoByBitmap(it) }
            }
            if (avatarFile?.exists() == true) {
                currentFileName = avatarFile.name
                viewModel.onUpdateAvatar(avatarFile) {
                    StyleableToast.makeText(requireContext(), it, R.style.toast_success).show()
                }
            }
        }
    }

    override fun setupDataBinding() {
        binding.fragment = this
    }

    override fun initViews() {}

    override fun observeViewModel() {
        viewModel.messageResponse.observeNetworkResult(
            onSuccess = { requireDeleteFile(it.data) },
            onError = { requireContext().showToastError(it) }
        )
    }

    private fun requireDeleteFile(message: String) = lifecycleScope.launch {
        fileInternalHelper.deletePhoto(currentFileName).also { deleted ->
            if (deleted) {
                onUpdateSuccess.invoke(message).also { this@ProfileUpdateAvatarBSDF.dismiss() }
            }
        }
    }

    fun onClickOpenGallery() {
        actionListener = openGallery.also {
            checkSDKTiramisu(
                onSDKTiramisu = {
                    requireContext().checkPermission(
                        permission = Manifest.permission.READ_MEDIA_IMAGES,
                        onGranted = { openImagePicker() },
                        onDenied = { requestPermission.launch(Manifest.permission.READ_MEDIA_IMAGES) }
                    )
                },
                onNotTiramisu = {
                    requireContext().checkPermission(
                        permission = Manifest.permission.READ_EXTERNAL_STORAGE,
                        onGranted = { openImagePicker() },
                        onDenied = { requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }
                    )
                }
            )
        }
    }

    private fun openImagePicker() {
        try {
            pickImageGallery.launch(
                Intent().apply {
                    action = Intent.ACTION_GET_CONTENT
                    type = "image/*"
                })
        } catch (e: Exception) {
            Logger.error(message = "pickImageGalleryLauncher: ${e.message}")
        }
    }

    fun onClickOpenCamera() {
        actionListener = openCamera.also {
            requireContext().checkPermission(
                permission = Manifest.permission.CAMERA,
                onGranted = { captureImageFromCamera() },
                onDenied = { requestPermission.launch(Manifest.permission.CAMERA) }
            )
        }
    }

    private fun captureImageFromCamera() {
        try {
            val takeImageIntentCapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takeImageCamera.launch(takeImageIntentCapture)
        } catch (e: Exception) {
            Logger.error(message = "takeImageCameraLauncher: ${e.message}")
        }
    }
}