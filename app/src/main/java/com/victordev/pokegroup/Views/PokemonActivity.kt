package com.victordev.pokegroup.Views

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.victordev.pokegroup.Adapters.RecyclerPokemonAllHolder
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.Interfaces.apiGexRetrofit
import com.victordev.pokegroup.ModelSerializado.Results
import com.victordev.pokegroup.Presenters.PokemonPresenterImpl
import com.victordev.pokegroup.R
import com.victordev.pokegroup.utils.App
import com.victordev.pokegroup.utils.ItemAnimation
import com.victordev.pokegroup.utils.Utils
import kotlinx.android.synthetic.main.activity_pokemon.*
import retrofit2.Retrofit
import javax.inject.Inject

class PokemonActivity : AppCompatActivity(), InterfaceMain.ViewPokemon {
    @Inject
    lateinit var funciones: Utils
    @Inject
    lateinit var retrofit: Retrofit
    var ctx: Context? = null
    val app = App()
    var conexion: Boolean? = false
    //variable para api de descraga
    var apiService: apiGexRetrofit? = null
    var animation_type = ItemAnimation.FADE_IN
    var dialog: Dialog? = null
    //variable de presentador
    var presenter: InterfaceMain.PresenterPokemon? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)
        ctx = this
        app.getNetComponent(Utils().decrypt(Utils().URL_PREFIJO), ctx!!, this)!!.inject(this)
        //inicializar presentador
        presenter = PokemonPresenterImpl(this)
        //inicializacion de cliente de descarga
        apiService = retrofit.create(apiGexRetrofit::class.java)
        //obtencion de conexion del dispositivo
        conexion = funciones.isNetworkAvailable(ctx!!)
        //fuentes
        //fuentes
        val logox = Typeface.createFromAsset(assets, "font/Fredoka.ttf")
        val mmedium = Typeface.createFromAsset(assets, "font/MontserratMedium.ttf")
        textpokedex.typeface = logox
        textpokemon.typeface = mmedium
        presenter!!.CallPokemons(conexion!!,apiService!!,ctx!!)

        //eventos onclick
        btnAtrasPok.setOnClickListener {
            finish()
        }
        modeDark()
    }
    //cargar datos
    override fun cargarData(mDatosPokemon: List<Results>) {
        if(mDatosPokemon.isNotEmpty()){
            linErrorPo.visibility = View.GONE
            ListaPokemon.visibility = View.VISIBLE
            ListaPokemon.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 2)
            val adapter =
                RecyclerPokemonAllHolder.RecyclerPokemonAllAdapter(mDatosPokemon, ctx!!, animation_type)
            ListaPokemon.adapter = adapter
            ListaPokemon.setHasFixedSize(true)
            adapter.notifyDataSetChanged()
        }else{
            linErrorPo.visibility = View.VISIBLE
            ListaPokemon.visibility = View.GONE
        }
    }
    //modedark
    @SuppressLint("SetTextI18n")
    fun modeDark() {
        if (Utils.modeDark) {
            fondoPoke.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.negro_grafito))
            textpokedex.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
            textpokemon.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(ctx!!, R.color.negro_grafito)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().decorView.systemUiVisibility = 0
                }
            }
        } else {
            fondoPoke.setBackgroundDrawable(resources.getDrawable(R.drawable.background_gradient))
            textpokedex.setTextColor(ContextCompat.getColor(ctx!!, R.color.negro_grafito))
            textpokemon.setTextColor(ContextCompat.getColor(ctx!!, R.color.grey_80))

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
}
