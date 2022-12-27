package ru.eababurin.pictureoftheday.model


import com.google.gson.annotations.SerializedName

data class PictureOfTheDayResponseData(
    @SerializedName("copyright") // по сути не нужны, потому как точно совпадают с именем переменной
    val copyright: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("explanation")
    val explanation: String,
    @SerializedName("hdurl")
    val hdurl: String,
    @SerializedName("media_type") // а тут нужен
    val mediaType: String,
    @SerializedName("service_version")
    val serviceVersion: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)