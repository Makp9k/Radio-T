package com.kvazars.radio_t.gitter.rest

import com.kvazars.radio_t.gitter.models.ChatMessage
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by lza on 11.03.2017.
 */
interface GitterRestApi {

    @GET("v1/rooms/{room}/chatMessages")
    fun getChatMessages(
            @Header("x-access-token") accessToken: String,
            @Path("room") room: String,
            @Query("skip") skip: Int?,
            @Query("beforeId") beforeId: String?,
            @Query("afterId") afterId: String?,
            @Query("aroundId") aroundId: String?,
            @Query("limit") limit: Int?,
            @Query("q") query: String?
    ): Single<List<ChatMessage>>

}