package com.victordev.pokegroup.Views

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.Presenters.LoginPresenterImpl
import com.victordev.pokegroup.R
import com.victordev.pokegroup.utils.App
import com.victordev.pokegroup.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(),InterfaceMain.ViewLogin {

    //inyect dependencia
    @Inject
    lateinit var funciones: Utils
    //dialogo
    var conexion: Boolean? = false
    var presenter: InterfaceMain.PresenterLogin? = null
    var ctx: Context? = null
    var dialog: Dialog? = null
    val app = App()
    val GOOGLE_LOG_IN_RC = 1
    private var callbackManager: CallbackManager? = null
    // Firebase Auth Object.
    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //inicializacion de datos
        ctx = this
        app.getNetComponent(Utils().decrypt(Utils().URL_PREFIJO), ctx!!, this)!!.inject(this)
        callbackManager = CallbackManager.Factory.create()
        //fuentes
        val mmedium = Typeface.createFromAsset(assets, "font/MontserratMedium.ttf")
        textoptions.typeface = mmedium
        textSesion.typeface = mmedium
        //firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        //inicializar presentador
        presenter = LoginPresenterImpl(this)
        //obtencion de conexion del dispositivo
        conexion = funciones.isNetworkAvailable(ctx!!)

        //eventos on click
        btnFacebook.setOnClickListener {
        presenter!!.loginFacebook(btnFacebookD)
        }
        btnGoogle.setOnClickListener {
        val signInIntent = presenter!!.loginGoogle()
            startActivityForResult(signInIntent, GOOGLE_LOG_IN_RC)
        }
        // modo dark
        btn_switch_home.setOnSwitchListener { isNight ->
            Utils.modeDark = isNight
            modeDark()
            guardarConfiguracion(Utils().isloaded(ctx!!), isNight)
        }
        presenter!!.initComponets(btnFacebookD,ctx!!,this,callbackManager!!)
        modeDark()
        configureDialog()
        btn_switch_home.setNight(Utils.modeDark)
        presenter!!.verificasesion()

    }
    //guardar conf
    private fun guardarConfiguracion(load: Boolean, dark: Boolean) {
        Utils().modoDark(ctx!!, dark, load)
    }
    //temas de app
    @SuppressLint("SetTextI18n")
    fun modeDark() {
        if (Utils.modeDark) {
            fondo.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.negro_grafito))
            textoptions.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
            textoptions.text = getString(R.string.claro)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(ctx!!, R.color.negro_grafito)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().decorView.systemUiVisibility = 0
                }
            }
        } else {
            fondo.setBackgroundDrawable(resources.getDrawable(R.drawable.background_gradient))
            textoptions.setTextColor(ContextCompat.getColor(ctx!!, R.color.text))
            textoptions.text = getString(R.string.oscuro)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(ctx!!, R.color.colorPrimaryDark)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    window.statusBarColor = Color.WHITE
                }
            }
        }
    }
    //configuracion de dialogo de carga
    fun configureDialog() {
        //inicializar dialogo
        dialog = Dialog(ctx!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.progress_dialog)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setCancelable(false)
    }

    /*funcion de intent*/
    fun irActividad() {
       val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in_trns, R.anim.fade_out_trns)
        finish()
    }

    override fun showDialog(boolean: Boolean) {
        if (boolean) {
            dialog!!.show()
        } else {
            dialog!!.dismiss()
        }
    }

    override fun irActividad(boolean: Boolean) {
        if(boolean){
            irActividad()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Login Facebook
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_LOG_IN_RC) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                presenter!!.firebaseAuthWithGoogle(result.signInAccount!!)
            } else {
                presenter!!.showError("Ha ocurrido un error.")
            }
        }
    }
}
