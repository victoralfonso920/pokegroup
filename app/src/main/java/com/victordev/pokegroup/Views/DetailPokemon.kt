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
import com.muddzdev.styleabletoastlibrary.StyleableToast
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.Interfaces.apiGexRetrofit
import com.victordev.pokegroup.ModelSerializado.PokemonData
import com.victordev.pokegroup.ModelSerializado.PokemonSpecie
import com.victordev.pokegroup.Presenters.DetPokemonPresenterImpl
import com.victordev.pokegroup.R
import com.victordev.pokegroup.utils.App
import com.victordev.pokegroup.utils.Utils
import kotlinx.android.synthetic.main.activity_detail_pokemon.*
import retrofit2.Retrofit
import javax.inject.Inject


class DetailPokemon : AppCompatActivity(), InterfaceMain.ViewDetPokemon {

    //inyect dependencia
    @Inject
    lateinit var funciones: Utils
    var ctx: Context? = null
    var dialog: Dialog? = null
    val app = App()
    var conexion: Boolean? = false

    @Inject
    lateinit var retrofit: Retrofit
    //variable de presentador
    var presenter: InterfaceMain.PresenterDetPokemon? = null
    //variable para api de desc
    var apiService: apiGexRetrofit? = null
    var logox : Typeface? = null
    var mlight : Typeface? = null
    var mmedium: Typeface? = null
    var mregular : Typeface? = null
    var numberpoke:String = ""

    var name: String = ""
    var hp: String = ""
    var height: String = ""
    var weight: String = ""
    var type: String = ""
    var region: String = ""
    var id: String = ""
    var urlImage: String = ""
    var exp: String = ""
    var pokemonSpecie:PokemonSpecie? = null
    var remover = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pokemon)
        //capturar data
        val extras = intent.extras
        if (extras != null) {
            numberpoke = extras.getString("data1","")
            region = extras.getString("data2","")
        }
        val url = "${Utils().decrypt(Utils().PREFIJO_DETAILS_POKE)}$numberpoke/"
        ctx = this
        app.getNetComponent(url, ctx!!, this)!!.inject(this)
        //inicializar presentador
        presenter = DetPokemonPresenterImpl(this)
        //inicializacion de cliente de descarga
        apiService = retrofit.create(apiGexRetrofit::class.java)
        //obtencion de conexion del dispositivo
        conexion = funciones.isNetworkAvailable(ctx!!)
        //fuentes
        val mmedium = Typeface.createFromAsset(assets, "font/MontserratMedium.ttf")

        cptext.typeface = mmedium
        textCpNumber.typeface = mmedium
        textName.typeface = mmedium
        healt.typeface = mmedium
        textType.typeface = mmedium
        textPeso.typeface = mmedium
        textTan.typeface = mmedium
        //eventos onclick
        btnAtrasDetP.setOnClickListener { finish() }
        closeDetP.setOnClickListener { finish() }
        agregar.setOnClickListener {
            if(remover){
                Utils.pokelist.remove(pokemonSpecie!!)
                Utils.selectedPositions.remove(id.toInt())
            }else{
                if(Utils.pokelist.size < 7){
                    Utils.pokelist.add(pokemonSpecie!!)
                    Utils.selectedPositions.add(id.toInt())
                }else{
                    showToastError("No puedes agregar mas pokemon al grupo", R.drawable.ic_error)
                }
            }
            verificarpokemon(id)
        }
        presenter!!.CallPokemons(conexion!!,apiService!!,ctx!!)
        modeDark()
    }

    fun verificarpokemon(id:String){
        if(Utils.pokelist.size > 0){
            for (poke in Utils.pokelist){
                if(poke.id.equals(id,true)){
                    agregar.text = getString(R.string.remover)
                    imgpokeBall.visibility = View.VISIBLE
                    remover = true
                    break
                }else{
                    agregar.text = getString(R.string.agregar_a_grupo)
                    imgpokeBall.visibility = View.GONE
                    remover = false
                }
            }
        }else{
            agregar.text = getString(R.string.agregar_a_grupo)
            imgpokeBall.visibility = View.GONE
            remover = false
        }

    }
    override fun cargarData(pokemon: PokemonData) {
        if(pokemon != null){
            urlImage = "${funciones.decrypt(funciones.PREFIJO_IMG)}${pokemon.id}.png"
            id = pokemon.id.toString()
            name = pokemon.name.capitalize()
            exp = pokemon.base_experience.toString()
            if(pokemon.types.size > 1){
                type = "${pokemon.types[0].type.name}/${pokemon.types[1].type.name}\nTipo"
            }else{
                type ="${pokemon.types[0].type.name}\nTipo"
            }
            weight = "${pokemon.weight} hgr\nPeso"
            height = "${pokemon.height} dm\nMedida"

            for(hep in pokemon.stats){
                if(hep.stat.name.toLowerCase().equals("hp",true)){
                    hp = hep.base_stat.toString()
                    break
                }
            }
            funciones.fondoUrl(ctx!!,urlImage,imgpokeDet)
            textName.text = name
            textCpNumber.text = exp
            arcSeekBar.progress = pokemon.base_experience
            textType.text = type
            textPeso.text = weight
            textTan.text = height
            healt.text = "HP$hp/$hp"


            pokemonSpecie = PokemonSpecie(
                name,
                hp,
                height,
                weight,
                type,
                region,
                id,
                exp,
                urlImage
            )
            if(region.equals("all",true)){
                agregar.text = getString(R.string.info)
                agregar.isEnabled = false
            }else{
                verificarpokemon(id)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun modeDark() {
        if (Utils.modeDark) {
            fondoDetail.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.negro_grafito))
            cptext.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
            textCpNumber.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(ctx!!, R.color.negro_grafito)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().decorView.systemUiVisibility = 0
                }
            }
        } else {
            fondoDetail.setBackgroundDrawable(resources.getDrawable(R.drawable.background_gradient))
            cptext.setTextColor(ContextCompat.getColor(ctx!!, R.color.negro_grafito))
            textCpNumber.setTextColor(ContextCompat.getColor(ctx!!, R.color.grey_80))

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
    //mostrar toast de error
    fun showToastError(mensaje: String, drawable: Int) {
        var st: StyleableToast = StyleableToast.Builder(ctx!!)
            .text(mensaje)
            .textColor(Color.WHITE)
            .backgroundColor(ContextCompat.getColor(ctx!!, R.color.Red))
            .icon(drawable)
            .build()
        st.show()
    }

}
