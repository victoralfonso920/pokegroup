package com.victordev.pokegroup.Presenters

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.victordev.pokegroup.Interactors.InteractorHomeImpl
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.Interfaces.apiGexRetrofit
import com.victordev.pokegroup.ModelSerializado.Result


class HomePresenterImpl(view: InterfaceMain.ViewHome):AppCompatActivity(),InterfaceMain.PresenterHome {

    //se obtiene la instancia de la vista
    private val vista = view
    private val interactor:InterfaceMain.InteractorHome = InteractorHomeImpl(this)

    override fun CallRegions(conexion: Boolean, api: apiGexRetrofit,ctx: Context) {
       interactor.CallRegions(conexion,api,ctx)
    }

    override fun cargarData(mDatosReservas: List<Result>) {
        if(vista != null){
            vista.cargarData(mDatosReservas)
        }
    }

}