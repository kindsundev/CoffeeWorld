package kind.sun.dev.coffeeworld.utils.storage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class FileInternalStorageUtil(
    private val context: Context
) {
    suspend fun savePhotoByUri(uri: Uri): File? {
        return withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val fileName = "photo_${System.currentTimeMillis()}.jpg"
                    val file = File(context.filesDir, fileName)
                    context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
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
        withContext(Dispatchers.IO) {

        }
        return null
    }

    suspend fun deletePhoto(filePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(filePath)
                if (file.exists()) {
                    file.delete()
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }



}