package com.victordev.pokegroup.utils

import android.app.Activity
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.victordev.pokegroup.Interfaces.DaggerInyectComponent
import com.victordev.pokegroup.Interfaces.InyectComponent
import com.victordev.pokegroup.LibModule.LibModule
import io.fabric.sdk.android.Fabric


class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        //Crashlytics Initialization
        Fabric.with(this, Crashlytics())
        //inicializacion sdk facebook
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }

    private lateinit var mNetComponent: InyectComponent

    fun getNetComponent(url: String, ctx: Context, actv: Activity): InyectComponent? {

        mNetComponent = DaggerInyectComponent.builder()
            .libModule(LibModule(url, ctx, actv))
            .build()
        return mNetComponent

    }
}