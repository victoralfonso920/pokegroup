package com.victordev.pokegroup.Views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.victordev.pokegroup.Adapters.AdapterListSectioned
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.ModelSerializado.GruposPoke
import com.victordev.pokegroup.ModelSerializado.Pokes
import com.victordev.pokegroup.R
import com.victordev.pokegroup.utils.App
import com.victordev.pokegroup.utils.DynamicLinksUtil
import com.victordev.pokegroup.utils.ItemAnimation
import com.victordev.pokegroup.utils.Utils
import kotlinx.android.synthetic.main.activity_grupos.*
import java.net.URLDecoder
import javax.inject.Inject

class GruposActivity : AppCompatActivity() {
    @Inject
    lateinit var funciones: Utils
    var ctx: Context? = null
    val app = App()
    var animation_type = ItemAnimation.FADE_IN
    var presenter: InterfaceMain.PresenterDetail? = null
    var conexion: Boolean? = false
    var dataReference: DatabaseReference? = null
    var logox: Typeface? = null
    var mmedium: Typeface? = null
    var nombreEquipo: String = ""
    var grupos = mutableListOf<GruposPoke>()
    var mAdapter: AdapterListSectioned? = null
    //llenar recycler con grupos
    val lista = mutableListOf<Pokes>()
    var device = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grupos)

        ctx = this
        app.getNetComponent("", ctx!!, this)!!.inject(this)

        //fuentes
        logox = Typeface.createFromAsset(assets, "font/Fredoka.ttf")
        mmedium = Typeface.createFromAsset(assets, "font/MontserratMedium.ttf")
        device = funciones.getDeviceId(ctx!!)

        textErrorGru.typeface = logox
        //obtencion de conexion del dispositivo
        conexion = funciones.isNetworkAvailable(ctx!!)
        dataReference = FirebaseDatabase.getInstance().reference
        btnAtrasG.setOnClickListener { finish() }
        modeDark()

    }

    override fun onStart() {
        super.onStart()

        dataReference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(ctx, "error en Base de Datos", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val ref = dataReference!!.child(device)
                ref.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        println(p0)
                    }
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            grupos.clear()
                            lista.clear()
                          //  for (data in p0.children) {
                                p0.children.mapNotNullTo(grupos) {
                                    it.getValue<GruposPoke>(GruposPoke::class.java)
                                }
                            //}
                            if (grupos.size > 0) {
                                linErrorGru.visibility = View.GONE
                                for (g in grupos) {
                                    val pokes = Pokes(
                                        "",
                                        g.nombre,
                                        g.region,
                                        true,
                                        "",
                                        "",
                                        "",
                                        "",
                                        g.id,
                                        "",
                                        g.urlRegion

                                    )
                                    lista.add(pokes)
                                    for (p in g.pokemon!!) {
                                        val poke = Pokes(
                                            p.urlImage,
                                            p.name,
                                            p.region,
                                            false,
                                            p.hp,
                                            p.height,
                                            p.weight,
                                            p.type,
                                            p.id,
                                            p.exp,
                                            ""
                                        )
                                        lista.add(poke)
                                    }
                                }
                                recyclerView.layoutManager = LinearLayoutManager(ctx!!)
                                recyclerView.setHasFixedSize(true)
                                //set data and list adapter
                                if (mAdapter == null) {
                                    mAdapter = AdapterListSectioned(ctx!!, lista, funciones, dataReference!!,device)
                                    recyclerView.adapter = mAdapter
                                }
                                mAdapter!!.notifyDataSetChanged()
                            } else {
                                mAdapter = null
                                recyclerView.adapter = mAdapter
                                linErrorGru.visibility = View.VISIBLE
                            }
                        }else{
                            linErrorGru.visibility = View.VISIBLE
                        }
                    }
                })

            }

        }
        )
    }

    fun actualizaName(id: String) {
        val ref = dataReference!!.child(device).orderByChild("id").equalTo(id)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0)
            }
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (data in p0.children) {
                        data.ref.child("nombre").setValue(nombreEquipo)
                    }
                }
            }
        })
    }

    //dialogo nombred e cuenta
    @SuppressLint("SetTextI18n")
    fun dialogoNombre(id: String, names: String) {
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
        editName.setText(names)
        ok_btn.setOnClickListener {
            if (editName.text.toString().isNotEmpty()) {
                nombreEquipo = editName.text.toString()
                val imm = ctx!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editName.windowToken, 0)
                actualizaName(id)
                alertDialog.hide()
            }
        }

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun editarGrupo(id: String, url: String, region: String, names1: String) {
        Utils.pokelist.clear()
        Utils.selectedPositions.clear()
        if (grupos.size > 0) {
            for (gr in grupos) {
                if (gr.id.equals(id, true)) {
                    for (pok in gr.pokemon!!) {
                        Utils.pokelist.add(pok)
                        Utils.selectedPositions.add(pok.id.toInt())
                    }
                    break
                }
            }
        }
        Utils.pokeId = id
        Utils.actualizar = true
        Utils.nombreGrupo = names1
        irOpcionesApp(DetailRegion::class.java, region, url)

    }

    //funcion de ir a actividad sin cerrar home
    fun irOpcionesApp(act: Class<*>, dta1: String, dta2: String) {
        val intent = Intent(applicationContext, act)
        intent.putExtra("data1", dta1)
        intent.putExtra("data2", dta2)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in_trns, R.anim.fade_out_trns)

    }

    //temas de la app
    @SuppressLint("SetTextI18n")
    fun modeDark() {
        if (Utils.modeDark) {
            gruposview.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.negro_grafito))
            textpokemon.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
            textErrorGru.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
            textpokedexGr.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(ctx!!, R.color.negro_grafito)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().decorView.systemUiVisibility = 0
                }
            }
        } else {
            gruposview.setBackgroundDrawable(resources.getDrawable(R.drawable.background_gradient))
            textpokemon.setTextColor(ContextCompat.getColor(ctx!!, R.color.negro_grafito))
            textErrorGru.setTextColor(ContextCompat.getColor(ctx!!, R.color.grey_80))
            textpokedexGr.setTextColor(ContextCompat.getColor(ctx!!, R.color.grey_80))

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
    fun onShareClicked(id:String) {
        val link = DynamicLinksUtil.generateContentLink(device,id)
        //val url = URLDecoder.decode(link.toString(),"UTF-8")
        val url = URLDecoder.decode(link.toString(),"UTF-8")
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(intent, "Share Link"))
    }

}
