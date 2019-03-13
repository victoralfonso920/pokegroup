package com.victordev.pokegroup.ModelSerializado

data class PokedexRegion(
    val descriptions: List<Description>,
    val id: Int,
    val is_main_series: Boolean,
    val name: String,
    val names: List<Names>,
    val pokemon_entries: List<PokemonEntry>,
    val region: Region,
    val version_groups: List<VersionGroups>
)

data class Names(
    val language: Languages,
    val name: String
)

data class Languages(
    val name: String,
    val url: String
)

data class Region(
    val name: String,
    val url: String
)

data class PokemonEntry(
    val entry_number: Int,
    val pokemon_species: PokemonSpecies
)

data class PokemonSpecies(
    val name: String,
    val url: String
)

data class Description(
    val description: String,
    val language: Language
)

data class VersionGroups(
    val name: String,
    val url: String
)