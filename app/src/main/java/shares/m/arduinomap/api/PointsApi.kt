package shares.m.arduinomap.api

import retrofit2.http.GET
import shares.m.arduinomap.models.Point

interface PointsApi {

    @GET("/points")
    suspend fun getPoints(): List<Point>

}