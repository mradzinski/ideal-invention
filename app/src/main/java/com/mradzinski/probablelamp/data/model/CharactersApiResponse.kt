package com.mradzinski.probablelamp.data.model

data class CharactersApiResponse(
    val results: List<Character>
) {
    companion object {
        fun empty() = CharactersApiResponse(emptyList())
    }
}