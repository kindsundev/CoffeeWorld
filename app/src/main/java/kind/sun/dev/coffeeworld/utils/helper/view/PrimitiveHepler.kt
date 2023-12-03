package kind.sun.dev.coffeeworld.utils.helper.view

import android.content.Context
import android.text.Editable

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Int.dpToPx(context: Context): Int {
    val density = context.resources.displayMetrics.density
    return (this * density + 0.5f).toInt()
}