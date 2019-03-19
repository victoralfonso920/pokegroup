package com.victordev.pokegroup.Views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.muddzdev.styleabletoastlibrary.StyleableToast
import com.victordev.pokegroup.Adapters.RecyclerPokedexHolder
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.Interfaces.apiGexRetrofit
import com.victordev.pokegroup.ModelSerializado.GruposPoke
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
    var animation_type = ItemAnimation.FADE_IN
    var presenter: InterfaceMain.PresenterDetail? = null
    var conexion: Boolean? = false
    // Firebase Auth Object.
    var firebaseAuth: FirebaseAuth? = null
    var device = ""
    //variable para api de descraga
    var apiService: apiGexRetrofit? = null
    var region:String = ""
    var urlRegion:String = ""
    var nombreEquipo:String = ""
    var dialog: Dialog? = null
    var adapter :RecyclerPokedexHolder.RecyclerPokedexAdapter? = null
    var dataReference:DatabaseReference? = null
    var logox: Typeface? = null
    var mmedium: Typeface? = null
    var actualizar = false
    @Inject
    lateinit var retrofit: Retrofit

    @RequiresApi(Build.VERSION_CODES.HONEYCOMB_MR1)
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
        device = funciones.getDeviceId(ctx!!)
        println(region)
        val resourceId:Int = ctx!!.resources.getIdentifier(region.toLowerCase(), "drawable", ctx!!.packageName)
        if(resourceId > 0){
            ImgRegion.visibility = View.VISIBLE
            funciones.fondo(ctx!!,resourceId,ImgRegion)
        }else{
            ImgRegion.visibility = View.GONE
            imgholder.visibility = View.VISIBLE
        }
        //fuentes
         logox = Typeface.createFromAsset(assets, "font/Fredoka.ttf")
         mmedium = Typeface.createFromAsset(assets, "font/MontserratMedium.ttf")
        textIndicacion.typeface = mmedium
        btnAceptarGrupo.typeface = mmedium
        textError.typeface = logox
        //eventos on click
        btnAtras.setOnClickListener {
            finish()
        }
        btnAceptarGrupo.setOnClickListener {
            if(Utils.pokelist.size < 3){
                showToastError("Debes de seleccionar al menos 3 pokemon", R.drawable.ic_error)
            }else if(Utils.pokelist.size in 3..6){
                if(nombreEquipo.isEmpty()){
                    dialogoNombre()
                }else{
                    guardarFirebase()
                }

            }
        }
        configureDialog()
        modeDark()
        presenter!!.CallPokemos(conexion!!,apiService!!,ctx!!)
        dialog!!.show()
        if(Utils.nombreGrupo.isNotEmpty()){
            nombreEquipo = Utils.nombreGrupo
            Utils.nombreGrupo = ""
        }

        if(Utils.actualizar){
            btnAceptarGrupo.text = getString(R.string.actu_grupo)
            Utils.actualizar = false
            actualizar = true
        }

    }
        @SuppressLint("SimpleDateFormat")
        fun guardarFirebase(){
            dataReference = FirebaseDatabase.getInstance().reference
            if(Utils.pokeId.isEmpty()){
              val id = dataReference!!.push().key
                val grupo = GruposPoke(id!!,
                    nombreEquipo,
                    region,
                    urlRegion,
                    Utils.pokelist)
                dataReference!!.child(device).child(id).setValue(grupo)
                dialogoSucess("Grupo guardado\ndeseas revisarlo")
                //Toast.makeText(ctx, "Grupo guardado",Toast.LENGTH_LONG).show()
                Utils.pokelist.clear()
                Utils.selectedPositions.clear()
                adapter!!.notifyDataSetChanged()
            }else{
                val grupo = GruposPoke(Utils.pokeId,
                    nombreEquipo,
                    region,
                    urlRegion,
                    Utils.pokelist)
                dataReference!!.child(device).child(Utils.pokeId).setValue(grupo)
                dialogoSucess("Grupo actualizado\ndeseas revisarlo")
                //Toast.makeText(ctx, "Grupo actualizado",Toast.LENGTH_LONG).show()
                Utils.pokeId = ""
                Utils.pokelist.clear()
                Utils.selectedPositions.clear()
                adapter!!.notifyDataSetChanged()
            }


        }
    override fun cargarData(mDatosPoquedex: List<PokemonEntry>) {
        linError.visibility = View.GONE
        GridCentral.visibility = View.VISIBLE
        GridCentral.layoutManager = GridLayoutManager(this, 3)
         adapter = RecyclerPokedexHolder.RecyclerPokedexAdapter(mDatosPoquedex, ctx!!, animation_type,region,Utils.selectedPositions)
        GridCentral.adapter = adapter
        GridCentral.setHasFixedSize(true)
        adapter!!.notifyDataSetChanged()
        dialog!!.dismiss()
    }

    @SuppressLint("SetTextI18n")
    fun modeDark() {
        if (Utils.modeDark) {
            fondoRegion.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.negro_grafito))
            textIndicacion.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
            textError.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))

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
            textError.setTextColor(ContextCompat.getColor(ctx!!, R.color.grey_80))
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
        btnAceptarGrupo.visibility = View.GONE
        dialog!!.dismiss()
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

    //configuracion de dialogo de carga
    fun configureDialog() {
        //inicializar dialogo
        dialog = Dialog(ctx!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.progress_dialog)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setCancelable(false)
    }

    override fun onResume() {
        super.onResume()
        if(adapter != null){
            adapter!!.notifyDataSetChanged()
        }
    }

    //dialogo nombred e cuenta
    @SuppressLint("SetTextI18n")
    fun dialogoNombre() {
        val alert = AlertDialog.Builder(ctx!!, R.style.hidetitle)
        val layout: LayoutInflater = this.layoutInflater
        val dialogo: View = layout.inflate(R.layout.dialogo_nombre, null)
        alert.setView(dialogo)
        val alertDialog: AlertDialog = alert.create()
        val window: Window = alertDialog.window
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val ok_btn: Button = dialogo.findViewById(R.id.btnSi)
        val editName: EditText = dialogo.findViewById(R.id.name)
        val titulo: TextView = dialogo.findViewById(R.id.mensaje_name)
        //propiedades de textos
        titulo.typeface = logox
        ok_btn.typeface = mmedium
        editName.typeface = mmedium
        ok_btn.setOnClickListener {
            if (editName.text.toString().isNotEmpty()) {
                 nombreEquipo = editName.text.toString()
                val imm = ctx!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editName.windowToken, 0)
                guardarFirebase()
                alertDialog.dismiss()
            } else {
                showToastError("Debes de agregar un nombre de cuenta", R.drawable.ic_error)
            }
        }

        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    //dialogo nombred e cuenta
    @SuppressLint("SetTextI18n")
    fun dialogoSucess(mensaje:String) {
        val alert = AlertDialog.Builder(ctx!!, R.style.hidetitle)
        val layout: LayoutInflater = this.layoutInflater
        val dialogo: View = layout.inflate(R.layout.dialogo_creado, null)
        alert.setView(dialogo)
        val alertDialog: AlertDialog = alert.create()
        val window: Window = alertDialog.window
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val ok_btn: Button = dialogo.findViewById(R.id.btnSi)
        val btnNp: Button = dialogo.findViewById(R.id.btnNp)
        val editName: TextView = dialogo.findViewById(R.id.name)
        val titulo: TextView = dialogo.findViewById(R.id.mensaje_name)

        editName.text = mensaje
        //propiedades de textos
        titulo.typeface = logox
        ok_btn.typeface = mmedium
        editName.typeface = mmedium
        ok_btn.setOnClickListener {
                val imm = ctx!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editName.windowToken, 0)
                alertDialog.dismiss()
                if(actualizar){
                    finish()
                }else{
                    irOpcionesApp(GruposActivity::class.java)
                }

        }
        btnNp.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    //funcion de ir a actividad sin cerrar home
    fun irOpcionesApp(act: Class<*>){
        val intent = Intent(applicationContext, act)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in_trns, R.anim.fade_out_trns)
        finish()
    }


}
