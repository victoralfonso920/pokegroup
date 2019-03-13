package com.victordev.pokegroup.ModelSerializado

data class PokemonsAll(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Results>
)

data class Results(
    val name: String,
    val url: String
)