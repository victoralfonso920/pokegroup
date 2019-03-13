package com.victordev.pokegroup.ModelSerializado

data class PokemonRegion(
    val id: Int,
    val locations: List<Location>,
    val main_generation: MainGeneration,
    val name: String,
    val names: List<Name>,
    val pokedexes: List<Pokedexe>,
    val version_groups: List<VersionGroup>
)

data class Name(
    val language: Language,
    val name: String
)

data class Language(
    val name: String,
    val url: String
)

data class Location(
    val name: String,
    val url: String
)

data class MainGeneration(
    val name: String,
    val url: String
)

data class Pokedexe(
    val name: String,
    val url: String
)

data class VersionGroup(
    val name: String,
    val url: String
)