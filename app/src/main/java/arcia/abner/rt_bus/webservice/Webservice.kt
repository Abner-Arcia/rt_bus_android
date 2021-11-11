package arcia.abner.rt_bus.webservice

import arcia.abner.rt_bus.model.Bus
import arcia.abner.rt_bus.model.Route
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH

interface Webservice {

    @GET("route/")
    fun getRoutes(): Call<List<Route>>

    @PATCH("bus/{id}/")
    @FormUrlEncoded
    fun updateBusLocation(
        @Field("lat") lat: Double,
        @Field("lng") lng: Double,
    ): Call<Bus>
}