package com.mradzinski.probablelamp.data.interactor

import com.mradzinski.probablelamp.data.interactor.base.FlowInteractor
import com.mradzinski.probablelamp.data.interactor.base.InteractorParams
import com.mradzinski.probablelamp.data.model.Character
import com.mradzinski.probablelamp.data.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface GetCharactersByPageInteractor : FlowInteractor<List<Character>, InteractorParams> {
    companion object {
        const val PARAM_PAGE_KEY = "param.page"
    }
}

class GetCharactersByPageInteractorImpl(private val repository: HomeRepository) : GetCharactersByPageInteractor {

    override fun build(params: InteractorParams): Flow<List<Character>> {
        val page = params.getInt(GetCharactersByPageInteractor.PARAM_PAGE_KEY, 1)

        return repository
            .getCharactersByPage(page = page)
            .map { it.results }
    }
}