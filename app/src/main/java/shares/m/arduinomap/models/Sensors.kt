package shares.m.arduinomap.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sensors(

    @SerializedName("temperature")
    val temperature: Double,

    @SerializedName("humidity")
    val humidity: Double,

    @SerializedName("pressure")
    val pressure: Double

): Parcelable