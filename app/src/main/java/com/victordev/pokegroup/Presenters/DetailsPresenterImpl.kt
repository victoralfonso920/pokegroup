package com.victordev.pokegroup.Presenters

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.victordev.pokegroup.Interactors.InteractorDetailImpl
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.Interfaces.apiGexRetrofit
import com.victordev.pokegroup.ModelSerializado.Pokedexe
import com.victordev.pokegroup.ModelSerializado.PokemonEntry


class DetailsPresenterImpl(view: InterfaceMain.ViewDetail):AppCompatActivity(),InterfaceMain.PresenterDetail {


    //se obtiene la instancia de la vista
    private val vista = view
    private val interactor:InterfaceMain.InteractorDetail = InteractorDetailImpl(this)

    override fun CallPokemos(conexion: Boolean, api: apiGexRetrofit, ctx: Context) {
        interactor.CallPokemos(conexion,api,ctx)
    }

    override fun cargarData(mDatosPokemon: List<PokemonEntry>) {
        if(vista != null){
            vista.cargarData(mDatosPokemon)
        }
    }
    override fun CallPokedex(conexion: Boolean, api: apiGexRetrofit, ctx: Context) {
        interactor.CallPokedex(conexion,api,ctx)

    }

    override fun verificadata(mDatosPokemon: List<Pokedexe>) {
        if(vista != null){
            vista.verificadata(mDatosPokemon)
        }
    }
    override fun showError() {
        if(vista != null){
            vista.showError()
        }
    }


}