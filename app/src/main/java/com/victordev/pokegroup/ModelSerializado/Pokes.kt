package com.victordev.pokegroup.ModelSerializado

data class Pokes (
    var image: String,
    var names: String,
    var region: String,
    var section:Boolean,
    var hp: String = "",
    var height: String = "",
    var weight: String = "",
    var type: String = "",
    var id: String = "",
    var exp: String = "",
    var url: String = ""
)