package kind.sun.dev.coffeeworld.data.model.response.cafe

data class ListCafeResponse(
    val `data`: List<CafeModel>,
    val success: Boolean
)