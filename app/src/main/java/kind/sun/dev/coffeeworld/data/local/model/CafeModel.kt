package kind.sun.dev.coffeeworld.data.local.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CafeModel(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val location: String,
    val rating: Double,
    @SerializedName("business_hours")
    val businessHours: String,
): Parcelable