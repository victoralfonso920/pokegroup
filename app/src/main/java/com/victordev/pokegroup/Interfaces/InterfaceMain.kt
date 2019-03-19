package com.victordev.pokegroup.Interfaces

import android.content.Context
import android.content.Intent
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.victordev.pokegroup.ModelSerializado.*
import com.victordev.pokegroup.Views.MainActivity

interface InterfaceMain {
    //pantalla login
    interface ViewLogin {
        fun showDialog(boolean: Boolean)
        fun irActividad(boolean: Boolean)

    }

    interface PresenterLogin {
        fun irActividad(boolean: Boolean)
        fun initComponets(
            btnFacebookD: LoginButton,
            ctx: Context,
            mainActivity: MainActivity,
            callbackManager: CallbackManager
        )
        fun loginGoogle():Intent
        fun loginFacebook(btnFacebookD: LoginButton)
        fun verificasesion()
        fun dismissDialog(boolean: Boolean)
        fun firebaseAuthWithGoogle(signInAccount: GoogleSignInAccount)
        fun showError(mensaje:String)
    }

    interface InteractorLogin {

        fun initComponet(btnFacebookD: LoginButton,ctx:Context,mainActivity: MainActivity,callbackManager: CallbackManager)
        fun loginGoogle(): Intent
        fun loginFacebook(btnFacebookD: LoginButton)
        fun verificasesion()
        fun firebaseAuthWithGoogle(signInAccount: GoogleSignInAccount)
        fun showError(mensaje:String)
    }

    //interfaces Home
    interface ViewHome{
        fun cargarData(mDatosReservas: List<Result>)
    }
    interface PresenterHome{
        fun CallRegions(conexion:Boolean,api:apiGexRetrofit,ctx:Context)
        fun cargarData(mDatosReservas: List<Result>)
    }
    interface InteractorHome{
        fun CallRegions(conexion:Boolean,api:apiGexRetrofit,ctx: Context)

    }

    //interfaces Detail
    interface ViewDetail{
        fun cargarData(mDatosReservas: List<PokemonEntry>)
        fun verificadata(mDatosReservas: List<Pokedexe>)
        fun showError()

    }
    interface PresenterDetail{
        fun CallPokemos(conexion:Boolean,api:apiGexRetrofit,ctx:Context)
        fun CallPokedex(conexion:Boolean,api:apiGexRetrofit,ctx:Context)
        fun cargarData(mDatosReservas: List<PokemonEntry>)
        fun verificadata(mDatosReservas: List<Pokedexe>)
        fun showError()
    }
    interface InteractorDetail{
        fun CallPokemos(conexion:Boolean,api:apiGexRetrofit,ctx: Context)
        fun CallPokedex(conexion:Boolean,api:apiGexRetrofit,ctx: Context)

    }

    //interface pokemon activity
    interface ViewPokemon{
        fun cargarData(mDatosReservas: List<Results>)
    }
    interface PresenterPokemon{
        fun CallPokemons(conexion:Boolean,api:apiGexRetrofit,ctx:Context)
        fun cargarData(mDatosReservas: List<Results>)
    }
    interface InteractorPokemon{
        fun CallPokemons(conexion:Boolean,api:apiGexRetrofit,ctx: Context)

    }
    //interface pokemon activity
    interface ViewDetPokemon{
        fun cargarData(mDatosReservas: PokemonData)
    }
    interface PresenterDetPokemon{
        fun CallPokemons(conexion:Boolean,api:apiGexRetrofit,ctx:Context)
        fun cargarData(mDatosReservas: PokemonData)
    }
    interface InteractorDetPokemon{
        fun CallPokemons(conexion:Boolean,api:apiGexRetrofit,ctx: Context)

    }

}