package kind.sun.dev.coffeeworld.utils.helper.storage

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kind.sun.dev.coffeeworld.utils.common.Constants.LAST_SAVE_KEY
import kind.sun.dev.coffeeworld.utils.common.Constants.SHARED_PREFERENCES_KEY
import kind.sun.dev.coffeeworld.utils.common.Constants.USER_TOKEN_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PreferencesHelper @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    private val ioScope by lazy { CoroutineScope(Dispatchers.IO) }

    var userToken: String?
        get() = sharedPrefs.getString(USER_TOKEN_KEY, null)
        set(value) {
            ioScope.launch {
                sharedPrefs.edit { putString(USER_TOKEN_KEY, value) }
            }
        }

    var lastSave: String?
        get() = sharedPrefs.getString(LAST_SAVE_KEY, null)
        set(value) {
            ioScope.launch {
                sharedPrefs.edit { putString(LAST_SAVE_KEY, value) }
            }
        }
}