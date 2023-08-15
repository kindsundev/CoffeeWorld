package kind.sun.dev.coffeeworld.data.model.response.cafe

data class CafeListResponse(
    val `data`: List<CafeModel>,
    val success: Boolean
)