package com.victordev.pokegroup.Interactors

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.muddzdev.styleabletoastlibrary.StyleableToast
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.R
import com.victordev.pokegroup.Views.MainActivity
import java.util.*


class InteractorLoginImpl(presenter: InterfaceMain.PresenterLogin) : AppCompatActivity(),
    InterfaceMain.InteractorLogin {


    //variable de presentador
    private val presenter = presenter
    // Firebase Auth Object.
    var firebaseAuth: FirebaseAuth? = null
    // Google API Client object.
    var googleApiClient: GoogleApiClient? = null
    var btnFacebookDImpl: LoginButton? = null
    var contx:Context? = null

    override fun initComponet(btnFacebookD: LoginButton, ctx: Context, mainActivity: MainActivity,callbackManager: CallbackManager) {
        firebaseAuth = FirebaseAuth.getInstance()
        contx =ctx
        //Login Facebook
        configureGoogle(ctx, mainActivity)
        configBtnFacebook(btnFacebookD,callbackManager)
    }

    override fun loginGoogle(): Intent {
        return Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
    }

    override fun loginFacebook(btnFacebookD: LoginButton) {
        btnFacebookDImpl!!.performClick()
    }

    //configuracion btn google
    private fun configureGoogle(ctx: Context, Activity: MainActivity) {
        // Configure Google Sign In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ctx.getString(R.string.request_client_id))
            .requestEmail()
            .build()
        // Creating and Configuring Google Api Client.
        googleApiClient = GoogleApiClient.Builder(Activity)
            .enableAutoManage(Activity) { }
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
            .build()
    }

    //configracion de btn facebook
    private fun configBtnFacebook(
        btnFacebookD: LoginButton,
        callbackManager: CallbackManager
    ) {
        btnFacebookDImpl = btnFacebookD
        //boton facebook
        //solicitar permisos de lectura
        btnFacebookD.setReadPermissions(Arrays.asList("email"))
        btnFacebookD.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                //login exitoso
                handleFacebookAccessToken(result.accessToken)
                println("tpkennn ${result.accessToken}")
            }

            override fun onCancel() {
                //se cancelo operacion
                showToastError("Se cancelo operación.", R.drawable.ic_error)
                presenter.dismissDialog(false)
            }

            override fun onError(error: FacebookException) {
                showToastError("Error.", R.drawable.ic_error)
                presenter.dismissDialog(false)

            }
        })
    }

    //mostrar toast de error
    fun showToastError(mensaje: String, drawable: Int) {
        var st: StyleableToast = StyleableToast.Builder(contx!!)
            .text(mensaje)
            .textColor(Color.WHITE)
            .backgroundColor(ContextCompat.getColor(contx!!, R.color.Red))
            .icon(drawable)
            .build()
        st.show()
    }


     override fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth?.signInWithCredential(credential)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                //startActivity(Intent(this@MainActivity, MainActivity::class.java))
                val user = firebaseAuth?.currentUser
                println("User account ID ${user?.uid}")
                println("Display Name : ${user?.displayName}")
                println("Email : ${user?.email}")
                println("Photo URL : ${user?.photoUrl}")
                println("Provider ID : ${user?.providerId}")
                presenter.irActividad(true)
            } else {
                showToastError("Ha ocurrido un error.", R.drawable.ic_error)
                presenter.dismissDialog(false)
            }
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = firebaseAuth!!.currentUser
                    println("User account ID ${user?.uid}")
                    println("Display Name : ${user?.displayName}")
                    println("Email : ${user?.email}")
                    println("Photo URL : ${user?.photoUrl}")
                    println("Provider ID : ${user?.providerId}")
                    presenter.irActividad(true)
                } else {
                    showToastError("Authentication failed.", R.drawable.ic_error)

                    presenter.dismissDialog(false)
                }
            }

    }

    //verificacion si existe sesion

    override fun verificasesion() {
        val mUser = FirebaseAuth.getInstance().currentUser
        if (mUser != null) {
            mUser.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result!!.token
                        if (idToken != null) {
                            presenter.irActividad(true)
                        }
                    } else {
                        showToastError("hay un error con la sesion", R.drawable.ic_error)
                    }
                }
        }
    }
    override fun showError(mensaje: String) {
        showToastError(mensaje, R.drawable.ic_error)
    }
}