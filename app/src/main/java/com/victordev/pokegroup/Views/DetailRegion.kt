package com.victordev.pokegroup.Views

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.victordev.pokegroup.Adapters.RecyclerPokedexHolder
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.Interfaces.apiGexRetrofit
import com.victordev.pokegroup.ModelSerializado.Pokedexe
import com.victordev.pokegroup.ModelSerializado.PokemonEntry
import com.victordev.pokegroup.Presenters.DetailsPresenterImpl
import com.victordev.pokegroup.R
import com.victordev.pokegroup.utils.App
import com.victordev.pokegroup.utils.ItemAnimation
import com.victordev.pokegroup.utils.Utils
import kotlinx.android.synthetic.main.activity_main2.*
import retrofit2.Retrofit
import javax.inject.Inject


class DetailRegion : AppCompatActivity(), InterfaceMain.ViewDetail {


    @Inject
    lateinit var funciones: Utils
    var ctx: Context? = null
    val app = App()
    // Firebase Auth Object.
    var firebaseAuth: FirebaseAuth? = null
    var conexion: Boolean? = false
    //variable para api de descraga
    var apiService: apiGexRetrofit? = null
    var region:String = ""
    var urlRegion:String = ""
    var animation_type = ItemAnimation.FADE_IN
    var presenter: InterfaceMain.PresenterDetail? = null
    var dialog: Dialog? = null

    @Inject
    lateinit var retrofit: Retrofit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        //capturar data
        val extras = intent.extras
        if (extras != null) {
            region = extras.getString("data1","")
            urlRegion = extras.getString("data2","")
        }
        ctx = this
        app.getNetComponent(urlRegion, ctx!!, this)!!.inject(this)
        //inicializar presentador
        presenter = DetailsPresenterImpl(this)
        //firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        //inicializacion de cliente de descarga
        apiService = retrofit.create(apiGexRetrofit::class.java)
        //obtencion de conexion del dispositivo
        conexion = funciones.isNetworkAvailable(ctx!!)

        val resourceId:Int = ctx!!.resources.getIdentifier(region, "drawable", ctx!!.packageName)
        if(resourceId > 0){
            ImgRegion.visibility = View.VISIBLE
            funciones.fondo(ctx!!,resourceId,ImgRegion)
        }else{
            ImgRegion.visibility = View.GONE
            imgholder.visibility = View.VISIBLE
        }
        //fuentes
        val logox = Typeface.createFromAsset(assets, "font/Fredoka.ttf")
        val mmedium = Typeface.createFromAsset(assets, "font/MontserratMedium.ttf")
        textIndicacion.typeface = mmedium
        textError.typeface = logox
        //eventos on click
        btnAtras.setOnClickListener {
            finish()
        }
        configureDialog()
        modeDark()
        presenter!!.CallPokemos(conexion!!,apiService!!,ctx!!)
        dialog!!.show()

    }

    override fun cargarData(mDatosPoquedex: List<PokemonEntry>) {
        linError.visibility = View.GONE
        GridCentral.visibility = View.VISIBLE
        GridCentral.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 2)
        val adapter =
            RecyclerPokedexHolder.RecyclerPokedexAdapter(mDatosPoquedex, ctx!!, animation_type,region)
        GridCentral.adapter = adapter
        GridCentral.setHasFixedSize(true)
        adapter.notifyDataSetChanged()
        dialog!!.dismiss()
    }

    @SuppressLint("SetTextI18n")
    fun modeDark() {
        if (Utils.modeDark) {
            fondoRegion.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.negro_grafito))
            textIndicacion.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(ctx!!, R.color.negro_grafito)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().decorView.systemUiVisibility = 0
                }
            }
        } else {
            fondoRegion.setBackgroundDrawable(resources.getDrawable(R.drawable.background_gradient))
            textIndicacion.setTextColor(ContextCompat.getColor(ctx!!, R.color.grey_80))
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
    override fun verificadata(mDatosReservas: List<Pokedexe>) {
        app.getNetComponent(mDatosReservas[0].url, ctx!!, this)!!.inject(this)
        apiService = retrofit.create(apiGexRetrofit::class.java)
        presenter!!.CallPokedex(conexion!!,apiService!!,ctx!!)
    }
    override fun showError() {
       linError.visibility = View.VISIBLE
        GridCentral.visibility = View.GONE
        dialog!!.dismiss()

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
}
