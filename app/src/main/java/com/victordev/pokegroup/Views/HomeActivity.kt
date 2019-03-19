package com.victordev.pokegroup.Views

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.victordev.pokegroup.Adapters.RecyclerRegionesHolder
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.Interfaces.apiGexRetrofit
import com.victordev.pokegroup.ModelSerializado.Result
import com.victordev.pokegroup.Presenters.HomePresenterImpl
import com.victordev.pokegroup.R
import com.victordev.pokegroup.utils.App
import com.victordev.pokegroup.utils.Utils
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Retrofit
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), InterfaceMain.ViewHome {


    //inyect dependencia
    @Inject
    lateinit var funciones: Utils
    var ctx: Context? = null
    var dialog: Dialog? = null
    val app = App()
    // Firebase Auth Object.
    var firebaseAuth: FirebaseAuth? = null
    var conexion: Boolean? = false

    @Inject
    lateinit var retrofit: Retrofit
    //variable de presentador
    var presenter: InterfaceMain.PresenterHome? = null
    //variable para api de descraga
    var apiService: apiGexRetrofit? = null
    var logox: Typeface? = null
    var mlight: Typeface? = null
    var mmedium: Typeface? = null
    var mregular: Typeface? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ctx = this
        app.getNetComponent(Utils().decrypt(Utils().URL_PREFIJO), ctx!!, this)!!.inject(this)

        //firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //inicializacion de cliente de descarga
        apiService = retrofit.create(apiGexRetrofit::class.java)
        //inicializar presentador
        presenter = HomePresenterImpl(this)
        //obtencion de conexion del dispositivo
        conexion = funciones.isNetworkAvailable(ctx!!)
        //fuentes
        logox = Typeface.createFromAsset(assets, "font/Fredoka.ttf")
        mlight = Typeface.createFromAsset(assets, "font/MontserratLight.ttf")
        mmedium = Typeface.createFromAsset(assets, "font/MontserratMedium.ttf")
        mregular = Typeface.createFromAsset(assets, "font/MontserratRegular.ttf")

        textEventos.typeface = logox
        textView.typeface = mregular
        textRegiones.typeface = mregular
        textMenu1.typeface = mmedium
        textMenu2.typeface = mmedium
        textMenu3.typeface = mmedium

        if (!Utils.isFirstLoad) {
            DialogoHelp()
        }
        //asignacion de avatar default
        funciones.fondoCirle(ctx!!, R.drawable.pikachu, avatar)
        //eventos on click
        avatar.setOnClickListener {
            showDialogProfile()
        }
        FloatingMyLocation.setOnClickListener {
            Utils.modeDark = !Utils.modeDark
            modeDark()
            guardarConfiguracion(Utils().isloaded(ctx!!), Utils.modeDark)
        }
        FloatingPokemon.setOnClickListener {
            irOpcionesApp(PokemonActivity::class.java, "", "")
        }
        FloatingGroups.setOnClickListener {
            irOpcionesApp(GruposActivity::class.java, "", "")
        }
        modeDark()
        //verificar usuario
        val mUser = FirebaseAuth.getInstance().currentUser
        if (mUser != null) {
            textEventos.append(" ${mUser.displayName}")
            if (mUser.photoUrl.toString().isNotEmpty()) {
                funciones.fondoCirleURL(ctx!!, mUser.photoUrl.toString(), avatar)
            }
        }
        presenter!!.CallRegions(conexion!!, apiService!!, ctx!!)
    }

    //guardar conf
    private fun guardarConfiguracion(load: Boolean, dark: Boolean) {
        Utils().modoDark(ctx!!, dark, load)
    }

    //temas de la app
    @SuppressLint("SetTextI18n")
    fun modeDark() {
        if (Utils.modeDark) {
            fondoHome.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.negro_grafito))
            FloatingMyLocation.setImageResource(R.drawable.ic_brightness_high_black_24dp)
            textEventos.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
            textView.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
            textRegiones.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
            textMenu1.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
            textMenu2.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
            textMenu3.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(ctx!!, R.color.negro_grafito)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().decorView.systemUiVisibility = 0
                }
            }
        } else {
            fondoHome.setBackgroundDrawable(resources.getDrawable(R.drawable.background_gradient))
            FloatingMyLocation.setImageResource(R.drawable.ic_brightness_2_black_24dp)
            textEventos.setTextColor(ContextCompat.getColor(ctx!!, R.color.negro_grafito))
            textView.setTextColor(ContextCompat.getColor(ctx!!, R.color.grey_80))
            textRegiones.setTextColor(ContextCompat.getColor(ctx!!, R.color.grey_80))
            textMenu1.setTextColor(ContextCompat.getColor(ctx!!, R.color.grey_80))
            textMenu2.setTextColor(ContextCompat.getColor(ctx!!, R.color.grey_80))
            textMenu3.setTextColor(ContextCompat.getColor(ctx!!, R.color.grey_80))
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

    //funcion de ir a actividad sin cerrar home
    fun irOpcionesApp(act: Class<*>, dta1: String, dta2: String) {
        val intent = Intent(applicationContext, act)
        intent.putExtra("data1", dta1)
        intent.putExtra("data2", dta2)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in_trns, R.anim.fade_out_trns)

    }

    //carga de datos de regiones
    override fun cargarData(mDatosReservas: List<Result>) {
        val adapter = RecyclerRegionesHolder.RecyclerRegionesAdapter(mDatosReservas, ctx!!)
        view_pager1.adapter = adapter
        adapter.notifyDataSetChanged()
        //asignacion de layoutmanager
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            ctx,
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
        view_pager1.layoutManager = linearLayoutManager
        view_pager1.setHasFixedSize(true)

        //rentalizado de scroll de recycler
        if (view_pager1.onFlingListener == null) {
            val snapHelper: SnapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(view_pager1)
        }
    }

    //mostrar dialogo de profile
    fun showDialogProfile() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_profile)
        val imagen = dialog.findViewById<ImageView>(R.id.imagen)
        val close = dialog.findViewById<ImageButton>(R.id.close)
        val nombreCand = dialog.findViewById<TextView>(R.id.nombreCand)
        val textDet = dialog.findViewById<TextView>(R.id.textDet)
        val btnSesion = dialog.findViewById<Button>(R.id.btnAceptar)
        nombreCand.typeface = logox
        textDet.typeface = mlight
        //obtener datos de usuario
        val mUser = FirebaseAuth.getInstance().currentUser
        if (mUser != null) {
            nombreCand.text = mUser.displayName
            textDet.text = mUser.email
            if (mUser.photoUrl.toString().isNotEmpty()) {
                funciones.fondoCirleURL(ctx!!, mUser.photoUrl.toString(), imagen)
            } else {
                funciones.fondoCirle(ctx!!, R.drawable.pikachu, imagen)
            }
        }
        btnSesion.setOnClickListener {
            firebaseAuth!!.signOut()
            LoginManager.getInstance().logOut()
            dialog.dismiss()
            irActividad()
        }
        close.setOnClickListener { dialog.dismiss() }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.show()
    }

    //funcion de envio a actividad
    fun irActividad() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in_trns, R.anim.fade_out_trns)
        finish()
    }

    fun DialogoHelp() {
        val dialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.setContentView(R.layout.dialog_help)
        val txthelp1: TextView = dialog.findViewById(R.id.txthelp1)
        val txthelp2: TextView = dialog.findViewById(R.id.txthelp2)
        val txthelp3: TextView = dialog.findViewById(R.id.txthelp3)
        val close: ImageView = dialog.findViewById(R.id.close)
        txthelp1.typeface = mmedium
        txthelp2.typeface = mmedium
        txthelp3.typeface = mmedium

        close.setOnClickListener {
            guardarConfiguracion(true, Utils().isdark(ctx!!))
            dialog.dismiss()
        }
        dialog.show()
    }

}
