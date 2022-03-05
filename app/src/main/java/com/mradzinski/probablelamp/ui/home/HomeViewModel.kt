package com.mradzinski.probablelamp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mradzinski.probablelamp.common.extension.copy
import com.mradzinski.probablelamp.common.viewmodel.FlowViewModel
import com.mradzinski.probablelamp.data.interactor.GetCharactersByPageInteractor
import com.mradzinski.probablelamp.data.interactor.base.interactorParams
import com.mradzinski.probablelamp.data.model.Character
import com.mradzinski.probablelamp.common.Failure
import com.mradzinski.probablelamp.common.Loading
import com.mradzinski.probablelamp.common.Success
import com.mradzinski.probablelamp.common.Uninitialized
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.inject

interface HomeViewModel {
    val state: StateFlow<HomeViewState>
    fun getCharacters(page: Int = 1)
    fun retry()
}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModelImpl() as T
    }
}

private class HomeViewModelImpl : FlowViewModel(), HomeViewModel {

    private val getCharactersByPageIterator: GetCharactersByPageInteractor by inject()

    override val state: MutableStateFlow<HomeViewState> = MutableStateFlow(HomeViewState.Loading)

    private var characters: MutableList<Character> = mutableListOf()
    private var currentPage: Int = 0

    /* ********************************************
     *              End of variables              *
     ******************************************** */

    init {
        getCharacters()
    }

    override fun getCharacters(page: Int) {
        val params = interactorParams {
            put(GetCharactersByPageInteractor.PARAM_PAGE_KEY, page)
        }

        currentPage = page

        getCharactersByPageIterator.build(params).execute { result ->
            when (result) {
                is Loading,
                is Uninitialized -> setState(HomeViewState.Loading)
                is Failure       -> setState(HomeViewState.Failure(result.error))
                is Success       -> {
                    characters.addAll(result.getWrappedValue())
                    setState(HomeViewState.Success(characters.copy()))
                }
            }
        }
    }

    override fun retry() = getCharacters(currentPage)

    private fun setState(newState: HomeViewState) {
        state.value = newState
    }
}