package kind.sun.dev.coffeeworld.utils

import android.util.Log

object Logger {

    private const val TAG = "DEBUG"

    fun error(message: String) {
        Log.e(TAG, message)
    }

    fun info(message: String) {
        Log.i(TAG, message)
    }

    fun warning(message: String) {
        Log.w(TAG, message)
    }
}