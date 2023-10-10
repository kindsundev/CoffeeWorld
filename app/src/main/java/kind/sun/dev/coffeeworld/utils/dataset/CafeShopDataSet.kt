package kind.sun.dev.coffeeworld.utils.dataset

class CafeShopDataSet {
    enum class Id {
        NEAR_HERE,
        FAR_AWAY
    }

    companion object {
        fun generateRandomDistance(index: Int): String {
            val random = (index.toFloat() * 0.35f) + (index.toFloat() / 2)
            return "From here ${String.format("%.2f", random)} km"
        }
    }
}