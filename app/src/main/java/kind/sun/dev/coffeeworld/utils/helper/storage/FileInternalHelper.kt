package kind.sun.dev.coffeeworld.utils.helper.storage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class FileInternalHelper(
    private val context: Context
) {

    suspend fun savePhotoByUri(uri: Uri): File? {
        return withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val file = createFile()
                    context.openFileOutput(file.name, Context.MODE_PRIVATE).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    if (file.exists()) file else null
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun savePhotoByBitmap(bitmap: Bitmap): File? {
        return withContext(Dispatchers.IO) {
            try {
                val file = createFile()
                context.openFileOutput(file.name, Context.MODE_PRIVATE).use { outputStream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                        throw IOException("Couldn't save bitmap")
                    }
                    file
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun createFile(): File {
        val fileName = "photo_${System.currentTimeMillis()}.jpg"
        return File(context.filesDir, fileName)
    }

    suspend fun deletePhoto(name: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                context.deleteFile(name)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }



}