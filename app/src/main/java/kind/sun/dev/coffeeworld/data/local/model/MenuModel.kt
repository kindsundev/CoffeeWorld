package kind.sun.dev.coffeeworld.data.local.model


data class MenuModel(
    val category: CategoryModel,
    val drinkList: List<DrinksModel>
)