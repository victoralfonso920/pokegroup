package com.victordev.pokegroup.Presenters

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.victordev.pokegroup.Interactors.InteractorDetPokemonImpl
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.Interfaces.apiGexRetrofit
import com.victordev.pokegroup.ModelSerializado.PokemonData

class DetPokemonPresenterImpl(view: InterfaceMain.ViewDetPokemon) : AppCompatActivity(), InterfaceMain.PresenterDetPokemon {

    //se obtiene la instancia de la vista
    private val vista = view
    private val interactor:InterfaceMain.InteractorDetPokemon= InteractorDetPokemonImpl(this)


    override fun CallPokemons(conexion: Boolean, api: apiGexRetrofit, ctx: Context) {
        interactor.CallPokemons(conexion,api,ctx)
    }

    override fun cargarData(mDatosReservas: PokemonData) {
        if(vista != null){
            vista.cargarData(mDatosReservas)
        }
    }


}