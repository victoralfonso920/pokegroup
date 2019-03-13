package com.victordev.pokegroup.Presenters

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.victordev.pokegroup.Interactors.InteractorPokemonImpl
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.Interfaces.apiGexRetrofit
import com.victordev.pokegroup.ModelSerializado.Results

class PokemonPresenterImpl(view: InterfaceMain.ViewPokemon) : AppCompatActivity(), InterfaceMain.PresenterPokemon {

    //se obtiene la instancia de la vista
    private val vista = view
    private val interactor:InterfaceMain.InteractorPokemon = InteractorPokemonImpl(this)


    override fun CallPokemons(conexion: Boolean, api: apiGexRetrofit, ctx: Context) {
        interactor.CallPokemons(conexion,api,ctx)
    }

    override fun cargarData(mDatosReservas: List<Results>) {
        if(vista != null){
            vista.cargarData(mDatosReservas)
        }
    }


}