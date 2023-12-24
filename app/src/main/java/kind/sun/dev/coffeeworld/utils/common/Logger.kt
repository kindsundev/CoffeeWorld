package kind.sun.dev.coffeeworld.utils.common

import android.util.Log

object Logger {

    private const val TAG = "DEBUG"
    const val API = "API_DEBUG"

    fun error(tag: String = TAG, message: String) {
        Log.e(tag, message)
    }

    fun info(tag: String = TAG, message: String) {
        Log.i(tag, message)
    }

    fun warning(tag: String = TAG, message: String) {
        Log.w(tag, message)
    }

    fun debug(tag: String = TAG, message: String) {
        Log.d(tag, message)
    }
}