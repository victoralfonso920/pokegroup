package com.victordev.pokegroup.ModelSerializado

data class GruposPoke (
    var id:String = "",
    var nombre:String = "",
    var region:String = "",
    var urlRegion:String = "",
    var pokemon:MutableList<PokemonSpecie>? = null

)