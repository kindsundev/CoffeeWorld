package kind.sun.dev.coffeeworld.data.remote.response

import kind.sun.dev.coffeeworld.data.local.model.MenuModel
import kind.sun.dev.coffeeworld.data.local.model.CafeModel
import kind.sun.dev.coffeeworld.data.local.model.CategoryModel
import kind.sun.dev.coffeeworld.data.local.model.DrinkModel

data class CafeResponse(
    val `data`: List<CafeModel>,
    val success: Boolean
)

data class CafeCategoryResponse(
    val `data`: List<CategoryModel>,
    val success: Boolean
)

data class CafeDrinksResponse(
    val `data`: List<DrinkModel>,
    val success: Boolean
)

data class CafeMenuResponse(
    val `data`: MenuModel,
    val success: Boolean
)

