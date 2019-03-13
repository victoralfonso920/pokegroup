package com.victordev.pokegroup.Views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.victordev.pokegroup.R
import com.victordev.pokegroup.utils.App
import com.victordev.pokegroup.utils.Utils
import kotlinx.android.synthetic.main.activity_splash_screen.*
import retrofit2.Retrofit
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject


class SplashScreen : AppCompatActivity() {
    //varable de conexion
    var conexion: Boolean? = false
    @Inject
    lateinit var retrofit: Retrofit
    @Inject
    lateinit var funciones: Utils
    private var contexto: Context? = null
    val app = App()
    var smalltobig: Animation? = null
    var fleft:Animation? = null
    var fhelper:Animation? = null
    internal val SPLASH_TIME_OUT = 5000
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //SOLICITAR PANTALLA COMPLETA
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash_screen)

        contexto = this
        try {
            app.getNetComponent(Utils().decrypt(Utils().URL_PREFIJO), contexto!!, this)!!.inject(this)
        } catch (e: Exception) {
            System.out.println("Error: $e")
        }

        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig)
        fleft = AnimationUtils.loadAnimation(this, R.anim.fleft)
        fhelper = AnimationUtils.loadAnimation(this, R.anim.fhelper)

        val logox = Typeface.createFromAsset(assets, "font/Fredoka.ttf")
        val mlight = Typeface.createFromAsset(assets, "font/MontserratLight.ttf")
        val mmedium = Typeface.createFromAsset(assets, "font/MontserratMedium.ttf")
        val mregular = Typeface.createFromAsset(assets, "font/MontserratRegular.ttf")

        ivLogo.typeface = logox
        ivSubtitle.typeface = mlight
        ivBtn.typeface = mmedium

        ivSplash.startAnimation(smalltobig)
        ivSplashBottom.startAnimation(smalltobig)

        ivLogo.translationX = 400f
        ivSubtitle.translationX = 400f
        ivBtn.translationX = 400f

        ivLogo.alpha = 0f
        ivSubtitle.alpha = 0f
        ivBtn.alpha = 0f

        ivLogo.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start()
        ivSubtitle.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(700).start()
        ivBtn.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(900).start()

        Utils.modeDark = Utils().isdark(contexto!!)
        Utils.isFirstLoad = Utils().isloaded(contexto!!)
        modeDark()
        timer()
       // getHash()
    }
    @SuppressLint("SetTextI18n")
    fun modeDark(){

        if(Utils.modeDark){
            ivSplash.setBackgroundColor(ContextCompat.getColor(contexto!!,
                R.color.negro_grafito
            ))
            ivSplashBottom.setBackgroundColor(ContextCompat.getColor(contexto!!,
                R.color.negro_grafito
            ))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(contexto!!,
                    R.color.negro_grafito
                )
            }
        }else{
            ivSplash.setBackgroundColor(ContextCompat.getColor(contexto!!, R.color.blanco))
            ivSplashBottom.setBackgroundColor(ContextCompat.getColor(contexto!!,
                R.color.redball
            ))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(contexto!!,
                    R.color.colorPrimaryDark
                )
            }
        }
    }
    //obtiene el hash para facebook
    @SuppressLint("PackageManagerGetSignatures")
    private fun getHash() {
        val info: PackageInfo
        try {
            info = this.packageManager.getPackageInfo("com.victordev.pokegroup", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                //String something = new String(Base64.encodeBytes(md.digest()));
                //Log.e("hash key:", something)
                println("hash key: $something")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
    }
    //timer de splash
    fun timer() {
        //timer de splah
        timer = object : CountDownTimer(SPLASH_TIME_OUT.toLong(), 1000) {                     //geriye sayma
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                vericaSesion()
            }
        }.start()
    }

    fun irActividad(act: Class<*>, valor: String) {
        intent = Intent(contexto, act)
        intent.putExtra("mensaje", valor)
        startActivity(intent)
        overridePendingTransition(
            R.anim.fleft,
            R.anim.fhelper
        )
        finish()
    }
    //funcion de llamada a pantalla de login
    override fun onDestroy() {
        super.onDestroy()
        if (timer != null) {
            timer!!.cancel()
        }
    }
    //verificar sesion
    fun vericaSesion(){
        var sesion = false
        val mUser = FirebaseAuth.getInstance().currentUser
        if (mUser != null) {
            mUser.getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken = task.result!!.token
                        if (idToken != null) {
                            sesion = true
                            llamarSesion(sesion)
                        }
                    }
                }
        }else{
            llamarSesion(sesion)
        }
    }
    //llama actividad segun sesion
    fun llamarSesion(sesion:Boolean){
        if(sesion){
            irActividad(HomeActivity::class.java, "")
        }else{
            irActividad(MainActivity::class.java, "")
        }
    }
}
