package kind.sun.dev.coffeeworld.utils.common

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

    fun debug(message: String) {
        Log.d(TAG, message)
    }
}