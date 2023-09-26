package kind.sun.dev.coffeeworld.utils.data

import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.ui.more.adapter.MoreRecyclerViewItem

class MoreOptionUtils {

    enum class Id {
        TERMS_AND_CONDITIONS,
        EXPLORE,
        HUNTER_FOR_DEALS,
        ORDER_REVIEWS,
        CONTACT_AND_FEEDBACK,
        USER,
        SECURITY,
        SETTINGS,
        PAYMENT
    }

    companion object {

        fun getMoreOptions(): List<MoreRecyclerViewItem> {
            return mutableListOf<MoreRecyclerViewItem>().apply {
                add(MoreRecyclerViewItem.Title(R.string.utility))
                add(
                    MoreRecyclerViewItem.Item(
                        Id.TERMS_AND_CONDITIONS, R.drawable.ic_baseline_article_24,
                        R.string.terms_and_conditions, MoreRecyclerViewItem.BOX_ONLY_TYPE
                    )
                )

                add(
                    MoreRecyclerViewItem.Item(
                        Id.EXPLORE, R.drawable.ic_baseline_dashboard_24,
                        R.string.explore, MoreRecyclerViewItem.BOX_DOUBLE_TYPE
                    )
                )
                add(
                    MoreRecyclerViewItem.Item(
                        Id.HUNTER_FOR_DEALS, R.drawable.ic_baseline_price_change_24,
                        R.string.hunt_for_deals, MoreRecyclerViewItem.BOX_DOUBLE_TYPE
                    )
                )
                add(MoreRecyclerViewItem.Temp)


                add(MoreRecyclerViewItem.Title(R.string.support))
                add(
                    MoreRecyclerViewItem.Item(
                        Id.ORDER_REVIEWS, R.drawable.ic_baseline_stars_24,
                        R.string.order_reviews, MoreRecyclerViewItem.ROW_TYPE
                    )
                )
                add(
                    MoreRecyclerViewItem.Item(
                        Id.CONTACT_AND_FEEDBACK, R.drawable.ic_baseline_message_24,
                        R.string.contact_and_feedback, MoreRecyclerViewItem.ROW_TYPE
                    )
                )

                add(MoreRecyclerViewItem.Title(R.string.account))
                add(
                    MoreRecyclerViewItem.Item(
                        Id.USER, R.drawable.ic_baseline_account_circle_24,
                        R.string.user, MoreRecyclerViewItem.ROW_TYPE
                    )
                )
                add(
                    MoreRecyclerViewItem.Item(
                        Id.SECURITY, R.drawable.ic_baseline_security_24,
                        R.string.security, MoreRecyclerViewItem.ROW_TYPE
                    )
                )
                add(
                    MoreRecyclerViewItem.Item(
                        Id.SETTINGS, R.drawable.ic_baseline_settings_24,
                        R.string.settings, MoreRecyclerViewItem.ROW_TYPE
                    )
                )
                add(
                    MoreRecyclerViewItem.Item(
                        Id.PAYMENT, R.drawable.ic_baseline_payment_24,
                        R.string.payment, MoreRecyclerViewItem.ROW_TYPE
                    )
                )
            }
        }
    }

}