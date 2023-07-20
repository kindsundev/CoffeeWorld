package kind.sun.dev.coffeeworld.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kind.sun.dev.coffeeworld.utils.Constants.SHARED_PREFS_TOKEN_FILE
import kind.sun.dev.coffeeworld.utils.Constants.USER_TOKEN
import javax.inject.Inject

class TokenManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPrefs =
        context.getSharedPreferences(SHARED_PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPrefs.edit()
            .putString(USER_TOKEN, token)
            .apply()
    }

    fun getToken(): String? {
        return sharedPrefs.getString(USER_TOKEN, null)
    }
}