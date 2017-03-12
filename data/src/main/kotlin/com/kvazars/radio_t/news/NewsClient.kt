package com.kvazars.radio_t.news

import com.kvazars.radio_t.news.models.ActiveNewsId
import com.kvazars.radio_t.news.models.NewsItem
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by lza on 12.03.2017.
 */
class NewsClient(httpClient: OkHttpClient = OkHttpClient()) {

    //region CONSTANTS -----------------------------------------------------------------------------

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private val newsApi = Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://news.radio-t.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NewsApi::class.java)

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    //endregion

    //region LOCAL METHODS -------------------------------------------------------------------------

    fun getNews(): Single<List<NewsItem>> {
        return newsApi.getNews()
    }

    fun getActiveNewsId(): Single<ActiveNewsId> {
        return newsApi.getActiveNewsId()
    }

    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------

    //endregion

}