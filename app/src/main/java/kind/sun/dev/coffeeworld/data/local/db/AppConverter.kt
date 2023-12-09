package kind.sun.dev.coffeeworld.data.local.db

import androidx.room.TypeConverter
import kind.sun.dev.coffeeworld.base.BaseJsonConverter
import kind.sun.dev.coffeeworld.data.local.model.BeverageCategoryModel
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.CategoryModel
import kind.sun.dev.coffeeworld.data.local.model.DrinkModel
import kind.sun.dev.coffeeworld.data.local.model.UserModel

class AppConverter {

    @TypeConverter
    fun cafesToJson(list: List<CafeModel>?): String? {
        return BaseJsonConverter.convertListToJson(list)
    }

    @TypeConverter
    fun jsonToCafes(json: String?): List<CafeModel>? {
        return BaseJsonConverter.convertJsonToList(json)
    }

    @TypeConverter
    fun userToJson(user: UserModel?): String? {
        return BaseJsonConverter.convertObjectToJson(user)
    }

    @TypeConverter
    fun jsonToUser(json: String?): UserModel? {
        return BaseJsonConverter.convertJsonToObject(json)
    }

    @TypeConverter
    fun beverageCategoriesToJson(list : List<BeverageCategoryModel>?) : String? {
        return BaseJsonConverter.convertListToJson(list)
    }

    @TypeConverter
    fun jsonToBeverageCategories(json: String?): List<BeverageCategoryModel>? {
        return BaseJsonConverter.convertJsonToList(json)
    }

    @TypeConverter
    fun drinksToJson(list: List<DrinkModel>?): String? {
        return BaseJsonConverter.convertListToJson(list)
    }

    @TypeConverter
    fun jsonToDrinks(json: String?): List<DrinkModel>? {
        return BaseJsonConverter.convertJsonToList(json)
    }

    @TypeConverter
    fun categoryToJson(category: CategoryModel?): String? {
        return BaseJsonConverter.convertObjectToJson(category)
    }

    @TypeConverter
    fun jsonToCategory(json: String?): CategoryModel? {
        return BaseJsonConverter.convertJsonToObject(json)
    }
}