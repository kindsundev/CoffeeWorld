package kind.sun.dev.coffeeworld.ui.more.user.profile

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kind.sun.dev.coffeeworld.databinding.FragmentAvatarBinding
import kind.sun.dev.coffeeworld.utils.common.Logger
import kind.sun.dev.coffeeworld.utils.common.checkPermission
import kind.sun.dev.coffeeworld.utils.common.checkSDKTiramisu
import java.io.ByteArrayOutputStream
import java.io.IOException

@AndroidEntryPoint
class AvatarFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentAvatarBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var pickImageGalleryLauncher:  ActivityResultLauncher<Intent>

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
            this::onPermissionResult
        )

        pickImageGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            this::onImagePickerResult
        )
    }

    private fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            openImagePicker()
        } else {
            Toast.makeText(requireContext(), "App needs photo and video permission for gallery access",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun onImagePickerResult(result: ActivityResult) {
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

    fun onClickOpenGallery() {
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}