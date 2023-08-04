package kind.sun.dev.coffeeworld.ui.more.user.profile

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.databinding.FragmentAvatarBinding
import kind.sun.dev.coffeeworld.utils.common.Constants
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.common.checkPermission
import kind.sun.dev.coffeeworld.utils.common.checkSDKTiramisu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class AvatarFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentAvatarBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var pickImageGalleryLauncher:  ActivityResultLauncher<Intent>
    private lateinit var takeImageCameraLauncher: ActivityResultLauncher<Intent>
    private var currentAction = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
                if (selectedImageUri != null) {
                    val base64 = uriToBase64(selectedImageUri, requireContext().contentResolver)
                    base64?.let { profileViewModel.updateAvatar(it) }
                }
            }
        } catch (e: Exception) {
            Logger.error("Error getting selected files ${e.message}")
        }
    }

    private fun uriToBase64(uri: Uri, contentResolver: ContentResolver): String? {
        try {
            val buffer = ByteArray(1024)
            val byteArrayOutputStream = ByteArrayOutputStream()

            contentResolver.openInputStream(uri)?.use {stream ->
                var byteRead: Int
                while(stream.read(buffer).also { byteRead = it } != -1) {
                    byteArrayOutputStream.write(buffer, 0, byteRead)
                }
            }

            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: IOException) {
            Logger.error(e.message.toString())
        }
        return null
    }

    private fun onTakeImageCameraResult(result: ActivityResult) {
        if (result.resultCode == FragmentActivity.RESULT_OK && result.data != null) {
            try {
                @Suppress("DEPRECATION") val bitmap = result.data?.extras?.get("data") as Bitmap
                handleImageResultFromCamera(bitmap)
            } catch (e: Exception) {
                Logger.error("takeImageCameraLauncher: ${e.message}")
            }
        }
    }

    private fun handleImageResultFromCamera(bitmap: Bitmap) {
        lifecycleScope.launch {
            val fileName = "image_${System.currentTimeMillis()}"
            val imageUri = savePhotoInternalStorage(fileName, bitmap)
            imageUri?.let { uri ->
                if (deletePhotoInternalStorage(fileName)) {
                    val base64 = uriToBase64(uri, requireContext().contentResolver)
                    base64?.let { profileViewModel.updateAvatar(it) }
                }
            }
        }
    }

    private suspend fun savePhotoInternalStorage(filename: String, bmp: Bitmap): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                requireContext().openFileOutput("$filename.jpg", Context.MODE_PRIVATE).use { stream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        throw IOException("Couldn't save bitmap")
                    }
                    File(requireContext().filesDir, "$filename.jpg").toUri()
                }
            } catch (e: Exception) {
                Logger.error("savePhotoInternalStorage: ${e.message}")
                null
            }
        }
    }

    private suspend fun deletePhotoInternalStorage(filename: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                requireContext().deleteFile(filename)
                true
            } catch (e: Exception) {
                false
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}