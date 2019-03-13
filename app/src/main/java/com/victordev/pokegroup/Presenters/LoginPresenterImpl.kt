package com.victordev.pokegroup.Presenters

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.victordev.pokegroup.Interactors.InteractorLoginImpl
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.Views.MainActivity

class LoginPresenterImpl(view: InterfaceMain.ViewLogin) : AppCompatActivity(), InterfaceMain.PresenterLogin {

    //se obtiene la instancia de la vista
    private val vista = view
    private val interactor: InterfaceMain.InteractorLogin = InteractorLoginImpl(this)


    override fun irActividad(boolean: Boolean) {
        if (vista != null) {
            vista.irActividad(boolean)
            vista.showDialog(false)
        }
    }

    override fun initComponets(
        btnFacebookD: LoginButton,
        ctx: Context,
        mainActivity: MainActivity,
        callbackManager: CallbackManager
    ) {
        interactor.initComponet(btnFacebookD,ctx,mainActivity,callbackManager)
    }

    override fun loginGoogle(): Intent {
        if (vista != null) {
            vista.showDialog(true)
        }
        return interactor.loginGoogle()
    }

    override fun loginFacebook(btnFacebookD: LoginButton) {
        if (vista != null) {
            vista.showDialog(true)
            interactor.loginFacebook(btnFacebookD)
        }
    }

    override fun verificasesion() {
        interactor.verificasesion()
    }
    override fun dismissDialog(boolean: Boolean) {
        if (vista != null) {
            vista.showDialog(boolean)
        }
    }
    override fun firebaseAuthWithGoogle(signInAccount: GoogleSignInAccount) {
        interactor.firebaseAuthWithGoogle(signInAccount)
    }
    override fun showError(mensaje: String) {
       interactor.showError(mensaje)
    }
}