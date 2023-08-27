package kind.sun.dev.coffeeworld.data.model.response.cafe

data class CafeDrinksResponse(
    val `data`: BeverageCategoryModel,
    val success: Boolean
)