package ru.eababurin.pictureoftheday.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfTheDayAPI {

    @GET("planetary/apod") // конкретный адрес API-запроса после доменного имени
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Call<PictureOfTheDayResponseData> // возвращаемый тип данных
}