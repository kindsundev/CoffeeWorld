package kind.sun.dev.coffeeworld.data.model.response.cafe

data class CafeDrinksResponse(
    val `data`: List<DrinksModel>,
    val success: Boolean
)