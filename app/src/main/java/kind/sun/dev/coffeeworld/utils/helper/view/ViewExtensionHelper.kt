package kind.sun.dev.coffeeworld.utils.helper.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kind.sun.dev.coffeeworld.R
import java.io.Serializable

inline fun <reified T: Serializable> Bundle.getSerializableSafe(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION")getSerializable(key) as T?
}

inline fun <reified T : Serializable> Intent.getSerializableSafe(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION")getSerializableExtra(key) as T?
}

inline fun <reified T: Parcelable> Bundle.getParcelableSafe(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION")getParcelable(key) as T?
}

inline fun <reified T: Parcelable> Intent.getParcelableSafe(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION")getParcelableExtra(key) as T?
}

inline fun checkSDKTiramisu(onSDKTiramisu: () -> Unit, onNotTiramisu: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        onSDKTiramisu()
    } else {
        onNotTiramisu()
    }
}

inline fun checkPermission (
    context: Context, permission: String, onGranted: () -> Unit, onDenied: () -> Unit)
{
    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
        onGranted()
    } else {
        onDenied()
    }
}

fun showAlertDialog(
    context: Context, title: String, message: String,
    onConfirmListener: () -> Unit
) {
    val dialog = MaterialAlertDialogBuilder(context, R.style.confirm_alert_dialog).apply {
        setTitle(title)
        setMessage(message)
        setCancelable(false)
        setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        setPositiveButton(R.string.ok) { dialog, _ ->
            onConfirmListener.invoke()
            dialog.dismiss()
        }
    }.create()
    dialog.window?.apply {
        setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setGravity(Gravity.CENTER)
    }
    dialog.show()
}