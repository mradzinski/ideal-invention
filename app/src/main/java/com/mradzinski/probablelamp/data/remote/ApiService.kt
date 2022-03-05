package com.mradzinski.probablelamp.data.remote

import com.mradzinski.probablelamp.data.model.CharactersApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("character/")
    fun getCharacters(@Query("page") page: Int): Flow<CharactersApiResponse>

}