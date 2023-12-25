package kind.sun.dev.coffeeworld.util.helper.view

import android.content.Context
import android.text.Editable

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Int.dpToPx(context: Context): Int {
    val density = context.resources.displayMetrics.density
    return (this * density + 0.5f).toInt()
}

fun Int.isExist(): Boolean = this != null && this != -1