package kind.sun.dev.coffeeworld.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cafe_table")
data class CafeModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val location: String,
    val rating: Double,
    @SerializedName("business_hours")
    val businessHours: String,
): Parcelable