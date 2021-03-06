package com.kvazars.radiot.data.news

import com.kvazars.radiot.data.news.models.ActiveNewsId
import com.kvazars.radiot.data.news.models.NewsItem
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by lza on 12.03.2017.
 */
interface NewsApi {

    @GET("api/v1/news")
    fun getNews(): Single<List<NewsItem>>

    @GET("api/v1/news/active")
    fun getActiveNewsId(): Single<ActiveNewsId>

}