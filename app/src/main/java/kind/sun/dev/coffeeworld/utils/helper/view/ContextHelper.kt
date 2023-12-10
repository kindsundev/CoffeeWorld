package kind.sun.dev.coffeeworld.utils.helper.view

import android.content.Context
import android.content.pm.PackageManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.github.muddz.styleabletoast.StyleableToast
import kind.sun.dev.coffeeworld.R

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

fun Context.showToast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, length).show()
}

fun Context.showSnackbar(
    root: CoordinatorLayout,
    resMessageId: Int,
    gravity: Int = Gravity.TOP or Gravity.CENTER_HORIZONTAL,
    duration: Int = Snackbar.LENGTH_SHORT,
    animMode: Int = BaseTransientBottomBar.ANIMATION_MODE_FADE,
    margin: Int = 16
) {
    Snackbar.make(root, getString(resMessageId), duration).apply {
        view.apply {
            findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.let {
                it.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
            layoutParams = (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
                this.gravity = gravity
                val marginPx = margin.dpToPx(context)
                setMargins(marginPx, marginPx, marginPx, marginPx)
            }
            animationMode = animMode
        }
    }.show()
}

fun Context.showAlertDialog(resTitleId: Int, resMessageId: Int, onConfirmListener: () -> Unit) {
    MaterialAlertDialogBuilder(this, R.style.confirm_alert_dialog).apply {
        setTitle(context.getString(resTitleId))
        setMessage(context.getString(resMessageId))
        setCancelable(false)
        setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
        setPositiveButton(R.string.ok) { dialog, _ ->
            onConfirmListener.invoke()
            dialog.dismiss()
        }
    }.create().also {
        it.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }
    }.show()
}

fun Context.checkPermission (permission: String, onGranted: () -> Unit, onDenied: () -> Unit) {
    if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
        onGranted()
    } else {
        onDenied()
    }
}

fun Context.showToastError(message: String) {
    StyleableToast.makeText(this, message, R.style.toast_error).show()
}

fun Context.showToastSuccess(message: String) {
    StyleableToast.makeText(this, message, R.style.toast_success).show()
}

fun Context.showToastNetwork(message: String) {
    StyleableToast.makeText(this, message, R.style.toast_network).show()
}

fun Context.showToastPermission(message: String) {
    StyleableToast.makeText(this, message, R.style.toast_permission).show()
}

fun Context.showToastWarning(message: String) {
    StyleableToast.makeText(this, message, R.style.toast_warning).show()
}

fun Context.showToastThanks(message: String) {
    StyleableToast.makeText(this, message, R.style.toast_thanks).show()
}