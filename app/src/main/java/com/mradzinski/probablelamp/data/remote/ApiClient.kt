package com.mradzinski.probablelamp.data.remote

import com.mradzinski.probablelamp.data.model.CharactersApiResponse
import kotlinx.coroutines.flow.Flow

interface ApiClient {

    fun getCharacters(page: Int = 1): Flow<CharactersApiResponse>

}

class ApiClientImpl(private val service: ApiService) : ApiClient {

    override fun getCharacters(page: Int): Flow<CharactersApiResponse> =
        service.getCharacters(page)

}