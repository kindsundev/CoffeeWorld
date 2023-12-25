package kind.sun.dev.coffeeworld.util.helper.storage

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kind.sun.dev.coffeeworld.util.common.Constants.CURRENT_CAFE_ID_KEY
import kind.sun.dev.coffeeworld.util.common.Constants.LAST_SAVE_KEY
import kind.sun.dev.coffeeworld.util.common.Constants.SHARED_PREFERENCES_KEY
import kind.sun.dev.coffeeworld.util.common.Constants.USER_TOKEN_KEY
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
                if (value?.isNotBlank() == true) {
                    sharedPrefs.edit { putString(USER_TOKEN_KEY, value) }
                }
            }
        }

    var lastSave: String?
        get() = sharedPrefs.getString(LAST_SAVE_KEY, null)
        set(value) {
            ioScope.launch {
                if (lastSave?.isNotBlank() == true) {
                    sharedPrefs.edit { putString(LAST_SAVE_KEY, value) }
                }
            }
        }

    var currentCafeId: Int?
        get() = sharedPrefs.getInt(CURRENT_CAFE_ID_KEY, -1)
        set(value) {
            ioScope.launch {
                sharedPrefs.edit {
                    value?.let { putInt(CURRENT_CAFE_ID_KEY, value) }
                }
            }
        }
}