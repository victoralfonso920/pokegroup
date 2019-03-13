package com.victordev.pokegroup.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.victordev.pokegroup.R


class Utils {
    //por seguridad se encripta en base64
    //https://www.base64encode.org/
    var URL_PREFIJO = "aHR0cHM6Ly9wb2tlYXBpLmNvL2FwaS92Mi8="
    val SUFUJO_REGION = "cmVnaW9uLw=="
    val PREFIJO_IMG = "aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL1Bva2VBUEkvc3ByaXRlcy9tYXN0ZXIvc3ByaXRlcy9wb2tlbW9uLw=="
    val SUFIJO_ALL_POKMON = "cG9rZW1vbg=="
    //variable dinamicas
    companion object {
        var modeDark = false
        var isFirstLoad = false
    }
    ///detectar si hay conexion
    fun isNetworkAvailable(contexto: Context): Boolean {
        val connectivityManager = contexto.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected

    }

    fun modoDark(ctx:Context,dark:Boolean,load:Boolean){
        val prefs = ctx.getSharedPreferences("conf", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("dark", dark)
        editor.putBoolean("load", load)
        editor.apply()
    }

    fun isdark(ctx:Context):Boolean{
        val prefs = ctx.getSharedPreferences("conf", Context.MODE_PRIVATE)
       return prefs.getBoolean("dark", false)
    }

    fun isloaded(ctx:Context):Boolean{
        val prefs = ctx.getSharedPreferences("conf", Context.MODE_PRIVATE)
        return prefs.getBoolean("load", false)
    }

    //fondos locales
    @SuppressLint("CheckResult")
    fun fondo(contexto: Context, id: Int, img: ImageView) {
        try {
            val options = RequestOptions()
                .centerCrop()
                .placeholder(id)
                .priority(Priority.HIGH)
                .format(DecodeFormat.PREFER_RGB_565)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .dontAnimate()
                .centerInside()

            Glide.with(contexto)
                .asBitmap()
                .load(id)
                .apply(options)
                .into(img)

        } catch (e: Exception) {
        }
    }
    @SuppressLint("CheckResult")
    //fondo de url
    fun fondoUrl(contexto: Context, id: String, img: ImageView) {
        try {
            val options = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.pokeball1)
                .priority(Priority.HIGH)
                .format(DecodeFormat.PREFER_RGB_565)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .dontAnimate()
                .centerInside()

            Glide.with(contexto)
                .asBitmap()
                .load(id)
                .apply(options)
                .into(img)

        } catch (e: Exception) {
        }
    }
    @SuppressLint("CheckResult")
    //fondo con trasformacion circular
    fun fondoCirle(contexto: Context, id: Int, img: ImageView) {
        try {
            val options = RequestOptions()
                .centerCrop()
                .placeholder(id)
                .priority(Priority.HIGH)
                .format(DecodeFormat.PREFER_RGB_565)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .dontAnimate()
                .centerInside()
                .circleCrop()

            Glide.with(contexto)
                .asBitmap()
                .load(id)
                .apply(options)
                .into(img)

        } catch (e: Exception) {
        }
    }
    @SuppressLint("CheckResult")
    fun fondoCirleURL(contexto: Context, id: String, img: ImageView) {
        try {
            val options = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.pikachu)
                .priority(Priority.HIGH)
                .format(DecodeFormat.PREFER_RGB_565)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .dontAnimate()
                .centerInside()
                .circleCrop()

            Glide.with(contexto)
                .asBitmap()
                .load(id)
                .apply(options)
                .into(img)

        } catch (e: Exception) {
        }
    }

    //encode and decode  string
    fun encrypt(string: String): String {
        // encode
        val encodeValue = android.util.Base64.encode(string.toByteArray(), android.util.Base64.DEFAULT)
        return String(encodeValue)
    }

    fun decrypt(string: String): String {
        //decode
        val decodeValue = android.util.Base64.decode(string, android.util.Base64.DEFAULT)
        return String(decodeValue)
    }

}