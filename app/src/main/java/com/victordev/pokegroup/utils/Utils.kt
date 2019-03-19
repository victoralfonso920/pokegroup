package com.victordev.pokegroup.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.victordev.pokegroup.ModelSerializado.PokemonSpecie
import com.victordev.pokegroup.R
import java.util.*


class Utils {
    //por seguridad se encripta en base64
    //https://www.base64encode.org/
    var URL_PREFIJO = "aHR0cHM6Ly9wb2tlYXBpLmNvL2FwaS92Mi8="
    val SUFUJO_REGION = "cmVnaW9uLw=="
    val PREFIJO_IMG = "aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL1Bva2VBUEkvc3ByaXRlcy9tYXN0ZXIvc3ByaXRlcy9wb2tlbW9uLw=="
    val SUFIJO_ALL_POKMON = "cG9rZW1vbg=="
    val PREFIJO_DETAILS_POKE = "aHR0cHM6Ly9wb2tlYXBpLmNvL2FwaS92Mi9wb2tlbW9uLw=="
    //variable dinamicas
    companion object {
        var modeDark = false
        var isFirstLoad = false
        val pokelist = mutableListOf<PokemonSpecie>()
        var selectedPositions: MutableList<Int> = ArrayList()
        var pokeId:String = ""
        var actualizar = false
        var nombreGrupo:String = ""

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

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        try {
            var id: String? = getUniqueID(context)
            if (id == null)
                id = android.provider.Settings.Secure.getString(context.contentResolver, android.provider.Settings.Secure.ANDROID_ID)
            return id!!
        } catch (e: Exception) {
        }

        return ""
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    private fun getUniqueID(context: Context): String {
        var telephonyDeviceId: String? = "NoTelephonyId"
        var androidDeviceId: String? = "NoAndroidId"
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            telephonyDeviceId = tm.deviceId
            if (telephonyDeviceId == null) {
                telephonyDeviceId = "NoTelephonyId"
            }
        } catch (e: Exception) {
        }
        try {
            androidDeviceId = android.provider.Settings.Secure.getString(context.contentResolver,
                android.provider.Settings.Secure.ANDROID_ID)
            if (androidDeviceId == null) {
                androidDeviceId = "NoAndroidId"
            }
        } catch (e: Exception) {
        }
        try {
            return (getStringIntegerHexBlocks(androidDeviceId!!.hashCode())
                    + "-"
                    + getStringIntegerHexBlocks(telephonyDeviceId!!.hashCode()))
        } catch (e: Exception) {
            return "0000-0000-1111-1111"
        }
    }

    fun getStringIntegerHexBlocks(value: Int): String {
        var result = ""
        var string = Integer.toHexString(value)
        val remain = 8 - string.length
        val chars = CharArray(remain)
        Arrays.fill(chars, '0')
        string = String(chars) + string
        var count = 0
        for (i in string.length - 1 downTo 0) {
            count++
            result = string.substring(i, i + 1) + result
            if (count == 4) {
                result = "-$result"
                count = 0
            }
        }
        if (result.startsWith("-")) {
            result = result.substring(1, result.length)
        }
        return result
    }

}