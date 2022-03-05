package com.mradzinski.probablelamp.data.repository

import com.mradzinski.probablelamp.data.model.CharactersApiResponse
import com.mradzinski.probablelamp.data.remote.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import retrofit2.HttpException

interface HomeRepository {
    fun getCharactersByPage(page: Int = 1): Flow<CharactersApiResponse>
}

internal class HomeRepositoryImpl(private val apiClient: ApiClient) : HomeRepository {

    private companion object {
        const val INVALID_PAGE_NUMBER_ERROR_CODE = 404
    }

    override fun getCharactersByPage(page: Int): Flow<CharactersApiResponse> = apiClient.getCharacters(page)
        .catch { e ->
            if (e is HttpException && e.code() == INVALID_PAGE_NUMBER_ERROR_CODE) {
                emit(CharactersApiResponse.empty())
            }
        }
}