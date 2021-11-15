package shares.m.arduinomap

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import shares.m.arduinomap.api.PointsApi

class MapApplication: Application() {

    lateinit var pointApi: PointsApi

    override fun onCreate() {
        super.onCreate()

        configureRetrofit()
    }

    private fun configureRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        pointApi = retrofit.create(PointsApi::class.java)
    }

    companion object {

        const val BASE_URL = "https://"

        const val TEST_URL = "https://91d53ff3-3a13-45c2-b690-8d62b28b7b57.mock.pstmn.io"

    }

}