package ru.eababurin.pictureoftheday.model

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.eababurin.pictureoftheday.utils.API_NASA_URL

class RepositoryImpl : Repository {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API_NASA_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    fun getPictureOfTheDayApi(): PictureOfTheDayAPI {
        return retrofit.create(PictureOfTheDayAPI::class.java)
    }
}