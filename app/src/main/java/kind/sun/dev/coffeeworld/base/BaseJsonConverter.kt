package kind.sun.dev.coffeeworld.base

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.ToNumberPolicy
import com.google.gson.reflect.TypeToken

object BaseJsonConverter {
    val gson : Gson by lazy {
        GsonBuilder()
            .serializeNulls()
            .setNumberToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER)
            .create()
    }

    @TypeConverter
    inline fun <reified T> convertJsonToList(json: String?): List<T>? {
        return try {
            gson.fromJson(json, object : TypeToken<List<T>>() {}.type)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }

    @TypeConverter
    inline fun <reified T> convertListToJson(list: List<T>?): String? {
        return try {
            gson.toJson(list, object : TypeToken<List<T>>() {}.type)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }

    @TypeConverter
    inline fun <reified T> convertJsonToObject(json: String?): T? {
        return try {
            gson.fromJson(json, T::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }

    @TypeConverter
    inline fun <reified T> convertObjectToJson(obj: T?): String? {
        return try {
            gson.toJson(obj)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }
}


