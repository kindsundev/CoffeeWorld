package kind.sun.dev.coffeeworld.util.dataset

import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.ui.adapter.order.menu.BannerItem

class OrderDataSet {

    companion object {
        const val SPRING_BANNER = 1
        const val SUMMER_BANNER = 2
        const val FALL_BANNER = 3
        const val WINTER_BANNER = 4
        const val INTERNAL_DAY_BANNER = 5

        fun getBannerItem(): List<BannerItem> {
            return mutableListOf<BannerItem>().apply {
                add(BannerItem(SPRING_BANNER, R.drawable.img_collection_spring))
                add(BannerItem(SUMMER_BANNER, R.drawable.img_collection_summer))
                add(BannerItem(FALL_BANNER, R.drawable.img_collection_fall))
                add(BannerItem(WINTER_BANNER, R.drawable.img_collection_winter))
                add(BannerItem(INTERNAL_DAY_BANNER, R.drawable.img_collection_internal_day))
            }
        }
    }
}