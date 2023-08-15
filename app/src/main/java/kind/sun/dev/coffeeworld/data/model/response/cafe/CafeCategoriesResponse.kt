package kind.sun.dev.coffeeworld.data.model.response.cafe

data class CafeCategoriesResponse(
    val `data`: List<CategoryModel>,
    val success: Boolean
)