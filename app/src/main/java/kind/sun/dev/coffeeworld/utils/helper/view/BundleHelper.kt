package kind.sun.dev.coffeeworld.utils.helper.view

import android.os.Build
import android.os.Bundle
import android.os.Parcelable


inline fun <reified T: Parcelable> Bundle.getParcelableHelper(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as T?
}

inline fun <reified T : Parcelable> Bundle.getParcelableArrayListHelper(key: String): ArrayList<T> {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            getParcelableArrayList(key, T::class.java)
        }
        else -> {
            @Suppress("DEPRECATION") getParcelableArrayList(key)
        }
    } as ArrayList<T>
}

