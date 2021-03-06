package com.victordev.pokegroup.utils

import android.net.Uri
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks


object DynamicLinksUtil {


    // [START ddl_generate_content_link]
    fun generateContentLink(nameDB:String, id:String): Uri {

        val baseUrl = Uri.parse("https://www.pokegro.com/activityone?data=$nameDB&id=$id")
        val domain = "https://pokegrop.page.link/pke"
        val link = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setLink(baseUrl)
                .setDomainUriPrefix(domain)
                .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
                .buildDynamicLink()


        return link.uri
    }
    // [END ddl_generate_content_link]

}
