package com.victordev.pokegroup.Interfaces


import com.victordev.pokegroup.LibModule.LibModule
import com.victordev.pokegroup.Views.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [LibModule::class])
interface InyectComponent {
    fun inject(activity: SplashScreen)
    fun inject(activity: MainActivity)
    fun inject(activity: HomeActivity)
    fun inject(activity: DetailRegion)
    fun inject(activity: PokemonActivity)
    fun inject(activity: DetailPokemon)
    fun inject(activity: GruposActivity)
}
