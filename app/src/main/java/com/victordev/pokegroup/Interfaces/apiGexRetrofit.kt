package com.victordev.pokegroup.Interfaces

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap


 interface apiGexRetrofit {
    //llamar con varios parametros en get
    @GET("{sufijourl}")
    fun getDatosGETSufijo(
            @Path("sufijourl") sufijourl: String
    ): Observable<Response<JsonObject>>

     @GET(" ")
     fun getDatosGET(): Observable<Response<JsonObject>>


    //llamar con varios parametros en get
    @GET("{sufijourl}")
    fun getDatosGET(
            @Path("sufijourl") sufijourl: String,
            @QueryMap options: Map<String, String>
    ): Observable<Response<JsonObject>>
    //llamar con varios parametros en get y header

    @GET("{sufijourl}")
    fun getDatosGETH(
            @Path("sufijourl", encoded = true) sufijourl: String,
            @QueryMap options: Map<String, String>
    ): Observable<Response<JsonObject>>

}