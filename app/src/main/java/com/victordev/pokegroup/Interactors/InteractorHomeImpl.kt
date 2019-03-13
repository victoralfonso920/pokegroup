package com.victordev.pokegroup.Interactors

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.muddzdev.styleabletoastlibrary.StyleableToast
import com.victordev.pokegroup.Interfaces.InterfaceMain
import com.victordev.pokegroup.Interfaces.apiGexRetrofit
import com.victordev.pokegroup.ModelSerializado.RegionesModel
import com.victordev.pokegroup.R
import com.victordev.pokegroup.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class InteractorHomeImpl(presenter: InterfaceMain.PresenterHome) : AppCompatActivity(), InterfaceMain.InteractorHome {


    //variable de presentador
    private val presenter = presenter
    private var ctxt:Context? = null
    var dialog: Dialog? = null

    @SuppressLint("CheckResult")
    override fun CallRegions(conexion: Boolean, api: apiGexRetrofit,ctx: Context) {
        ctxt = ctx
        configureDialog()
        if (conexion) {
            dialog!!.show()
            //add sufijo
            val sufijo = Utils().decrypt(Utils().SUFUJO_REGION)
            api.getDatosGETSufijo(sufijo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if(result.isSuccessful){
                        val mDatosReservas= Gson().fromJson<RegionesModel>(result.body().toString(), RegionesModel::class.java)
                        presenter.cargarData(mDatosReservas.results)
                    }else{
                        showToastError("Hubo un error al obtener los datos\n Intenta en otro momento", R.drawable.ic_error)
                    }
                    dialog!!.dismiss()
                }, { error ->
                    dialog!!.dismiss()
                    println(error.toString())
                    showToastError("Hubo un error al obtener los datos\n Intenta en otro momento", R.drawable.ic_error)
                })
        } else {
            dialogInter()
        }
    }

    //mostrar toast de error
    fun showToastError(mensaje: String, drawable: Int) {
        var st: StyleableToast = StyleableToast.Builder(ctxt!!)
            .text(mensaje)
            .textColor(Color.WHITE)
            .backgroundColor(ContextCompat.getColor(ctxt!!, R.color.Red))
            .icon(drawable)
            .build()
        st.show()
    }

    //dialogo fullscreen
    fun dialogInter() {
        val dialog = Dialog(ctxt!!, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.alert_dialog_inter)
        val tituloPermisos: TextView = dialog.findViewById(R.id.tituloPermisos)
        val textMensaje: TextView = dialog.findViewById(R.id.textMensaje)
        val btnAceptar: Button = dialog.findViewById(R.id.btnAceptar)

        val logox = Typeface.createFromAsset(ctxt!!.assets, "font/Fredoka.ttf")
        val mmedium = Typeface.createFromAsset(ctxt!!.assets, "font/MontserratMedium.ttf")
        tituloPermisos.typeface = logox
        textMensaje.typeface = mmedium
        btnAceptar.typeface = mmedium

        btnAceptar.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    //configuracion de dialogo de carga
    fun configureDialog() {
        //inicializar dialogo
        dialog = Dialog(ctxt!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.progress_dialog)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setCancelable(false)
    }
}