package kind.sun.dev.coffeeworld.data.model.response.cafe

data class BeverageCategoryModel(
    val categoryName: String,
    val drinksList: List<DrinksModel>
)