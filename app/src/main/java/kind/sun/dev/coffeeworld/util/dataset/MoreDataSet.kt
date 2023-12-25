package kind.sun.dev.coffeeworld.util.dataset


import kind.sun.dev.coffeeworld.R
import kind.sun.dev.coffeeworld.ui.adapter.more.MoreViewItem

class MoreDataSet {

    enum class Id {
        TERMS_AND_CONDITIONS,
        EXPLORE,
        HUNTER_FOR_DEALS,
        ORDER_REVIEWS,
        CONTACT_AND_FEEDBACK,
        USER,
        SECURITY,
        SETTINGS,
        PAYMENT,
        NAME,
        PROFILE,
        AVATAR,
        EMAIL,
        PASSWORD,
        ADDRESS,
        PHONE,
        SWITCH_ACCOUNT,
        LOG_OUT,
    }

    companion object {

        fun getMoreFragmentOptions(): List<MoreViewItem> {
            return mutableListOf<MoreViewItem>().apply {
                add(MoreViewItem.Title(R.string.utility))
                add(MoreViewItem.Item(Id.TERMS_AND_CONDITIONS, R.drawable.ic_baseline_article_24, R.string.terms_and_conditions, MoreViewItem.BOX_ONLY_TYPE))
                add(MoreViewItem.Item(Id.EXPLORE, R.drawable.ic_baseline_dashboard_24, R.string.explore, MoreViewItem.BOX_DOUBLE_TYPE))
                add(MoreViewItem.Item(Id.HUNTER_FOR_DEALS, R.drawable.ic_baseline_price_change_24, R.string.hunt_for_deals, MoreViewItem.BOX_DOUBLE_TYPE))
                add(MoreViewItem.Temp)
                add(MoreViewItem.Title(R.string.support))
                add(MoreViewItem.Item(Id.ORDER_REVIEWS, R.drawable.ic_baseline_stars_24, R.string.order_reviews, MoreViewItem.ROW_TYPE))
                add(MoreViewItem.Item(Id.CONTACT_AND_FEEDBACK, R.drawable.ic_baseline_message_24, R.string.contact_and_feedback, MoreViewItem.ROW_TYPE))
                add(MoreViewItem.Title(R.string.account))
                add(MoreViewItem.Item(Id.USER, R.drawable.ic_baseline_account_circle_24, R.string.user, MoreViewItem.ROW_TYPE))
                add(MoreViewItem.Item(Id.SECURITY, R.drawable.ic_baseline_security_24, R.string.security, MoreViewItem.ROW_TYPE))
                add(MoreViewItem.Item(Id.SETTINGS, R.drawable.ic_baseline_settings_24, R.string.settings, MoreViewItem.ROW_TYPE))
                add(MoreViewItem.Item(Id.PAYMENT, R.drawable.ic_baseline_payment_24, R.string.payment, MoreViewItem.ROW_TYPE))
            }
        }

        fun getProfileFragmentOptions(): List<MoreViewItem> {
            return mutableListOf<MoreViewItem>().apply {
                add(MoreViewItem.Temp)
                add(MoreViewItem.Title(R.string.dashboard))
                add(MoreViewItem.Item(Id.NAME, R.drawable.ic_baseline_account_circle_24, R.string.name, MoreViewItem.ROW_TYPE))
                add(MoreViewItem.Item(Id.EMAIL, R.drawable.ic_baseline_email_24, R.string.email, MoreViewItem.ROW_TYPE))
                add(MoreViewItem.Item(Id.PASSWORD, R.drawable.ic_baseline_lock_24, R.string.password, MoreViewItem.ROW_TYPE))
                add(MoreViewItem.Item(Id.ADDRESS, R.drawable.ic_baseline_home_24, R.string.address, MoreViewItem.ROW_TYPE))
                add(MoreViewItem.Item(Id.PHONE, R.drawable.ic_baseline_phone_24, R.string.phone, MoreViewItem.ROW_TYPE))
                add(MoreViewItem.Title(R.string.options))
                add(MoreViewItem.Item(Id.SWITCH_ACCOUNT, R.drawable.ic_baseline_switch_account_24, R.string.switch_account, MoreViewItem.ROW_TYPE))
                add(MoreViewItem.Item(Id.LOG_OUT, R.drawable.ic_baseline_exit_to_app_24, R.string.logout, MoreViewItem.ROW_TYPE))
            }
        }

    }

}