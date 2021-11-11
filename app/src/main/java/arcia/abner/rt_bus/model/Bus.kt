package arcia.abner.rt_bus.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Bus(
    val id: Int,
    val model: String,
    val color: String,
    val capacity: Int,
    val route: Int,
    val lat: Double,
    val lng: Double,
    @SerializedName("creation_date") val creationDate: String,
) : Serializable
