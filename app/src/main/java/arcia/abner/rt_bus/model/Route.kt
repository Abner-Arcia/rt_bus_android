package arcia.abner.rt_bus.model

import java.io.Serializable

data class Route(
    val name: String,
    val description: String,
    val points: List<Point>
) : Serializable