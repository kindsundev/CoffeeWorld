package kind.sun.dev.coffeeworld.data.remote.response

import kind.sun.dev.coffeeworld.data.local.model.BeverageCategoryModel
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.CategoryModel

data class CafeResponse(
    val `data`: List<CafeModel>,
    val success: Boolean
)

data class CafeCategoryResponse(
    val `data`: List<CategoryModel>,
    val success: Boolean
)

data class CafeDrinksResponse(
    val `data`: BeverageCategoryModel,
    val success: Boolean
)

