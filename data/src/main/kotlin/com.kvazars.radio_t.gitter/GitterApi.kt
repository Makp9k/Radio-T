package com.kvazars.radio_t.gitter

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by lza on 24.02.2017.
 */
interface GitterApi {

    @GET("{room}/~chat")
    fun getChatPage(@Path("room") room: String): Observable<ResponseBody>

}
