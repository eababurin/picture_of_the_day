package ru.eababurin.pictureoftheday.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.eababurin.pictureoftheday.BuildConfig
import ru.eababurin.pictureoftheday.model.PictureOfTheDayResponseData
import ru.eababurin.pictureoftheday.model.RepositoryImpl

class PictureOfTheDayViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData<AppState>(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()
) : ViewModel() {

    fun getLiveData(): MutableLiveData<AppState> {
        return liveData
    }

    fun sendRequest(date: String) {
        liveData.postValue(AppState.Loading)
        repositoryImpl.getPictureOfTheDayApi()
            .getPictureOfTheDayByDate(BuildConfig.NASA_API_KEY, date)
            .enqueue(callback)
    }

    private val callback = object : Callback<PictureOfTheDayResponseData> {
        override fun onResponse(
            call: Call<PictureOfTheDayResponseData>,
            response: Response<PictureOfTheDayResponseData>
        ) {
            if (response.isSuccessful) {
                liveData.postValue(AppState.Success(response.body()!!))
            } else {
                liveData.postValue(AppState.Error("Не удалось загрузить изображение"))

            }
        }

        override fun onFailure(call: Call<PictureOfTheDayResponseData>, t: Throwable) {}
    }

}