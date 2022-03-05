package com.mradzinski.probablelamp.ui.home

import com.mradzinski.probablelamp.data.model.Character


sealed class HomeViewState {
    object Loading : HomeViewState()
    data class Success(val characters: List<Character>) : HomeViewState()
    data class Failure(val error: Throwable) : HomeViewState()
}