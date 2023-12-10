package kind.sun.dev.coffeeworld.utils.helper.view

import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kind.sun.dev.coffeeworld.base.BaseViewModel
import kind.sun.dev.coffeeworld.utils.custom.CustomLoadingDialog
import kind.sun.dev.coffeeworld.utils.helper.animation.setScaleAnimation
import kotlinx.coroutines.CoroutineScope

fun View.hideKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(windowToken, 0)
}

fun View.setViewHeight(value: Int) { layoutParams.height = value }

fun View.remove() { visibility = View.GONE }

fun View.show() { visibility = View.VISIBLE }

fun View.hide() { visibility = View.INVISIBLE }

fun View.toggleNetworkErrorLayout(
    enableToggle: Boolean, enableMargin: Boolean = false, marginPx: Int = 0
) {
    if (enableToggle) {
        if (enableMargin) {
            layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(0, 0, 0, marginPx)
            }
        }
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}

fun View.setOnClickScaleListener(onClickAction : () -> Unit) {
    setOnClickListener {
        it.setScaleAnimation {
            onClickAction.invoke()
        }
    }
}

inline fun checkSDKTiramisu(onSDKTiramisu: () -> Unit, onNotTiramisu: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        onSDKTiramisu()
    } else {
        onNotTiramisu()
    }
}

fun TextView.showErrorMessage(message: String) {
    this.also {
        visibility = View.VISIBLE
        text = message
    }
}

fun SwipeRefreshLayout.checkToHide() {
    if (isRefreshing) isRefreshing = false
}

fun CustomLoadingDialog.checkToDismiss() { if (isAdded) dismiss() }
