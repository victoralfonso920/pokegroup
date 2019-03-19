package com.victordev.pokegroup.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victordev.pokegroup.ModelSerializado.PokemonEntry
import com.victordev.pokegroup.R
import com.victordev.pokegroup.Views.DetailPokemon
import com.victordev.pokegroup.utils.ItemAnimation
import com.victordev.pokegroup.utils.Utils
import java.util.regex.Pattern





class RecyclerPokedexHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    //instancia de controles
    val imagen: ImageView = itemView.findViewById(R.id.imagen)
    val imgpokeBall: ImageView = itemView.findViewById(R.id.imgpokeBall)
    val nombre: TextView = itemView.findViewById(R.id.nombre)
    val textRegiones: TextView = itemView.findViewById(R.id.textRegiones)
    val margen: LinearLayout = itemView.findViewById(R.id.margen)

    //adapters
    class RecyclerPokedexAdapter(
        private val pokedex: List<PokemonEntry>,
        private val ctx: Context,
        private val  animation_type: Int,
        private val  region: String,
        private var selectedPositions: MutableList<Int>
    )
        : RecyclerView.Adapter<RecyclerPokedexHolder>() {

        private val inflater: LayoutInflater = LayoutInflater.from(ctx)
        private val util: Utils = Utils()

        //se obtiene tamaño de dato segun tipo
        override fun getItemCount(): Int {
                return pokedex.size
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerPokedexHolder {
            val itemView = inflater.inflate(R.layout.item_pokemon, parent, false)
            return RecyclerPokedexHolder(itemView)

        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerPokedexHolder, position: Int) {
            val view = holder
            setAnimation(view.itemView, position)
            //fuentes
            val logox = Typeface.createFromAsset(ctx.assets, "font/Fredoka.ttf")
            val mmedium = Typeface.createFromAsset(ctx.assets, "font/MontserratMedium.ttf")
            holder.nombre.typeface = logox
            holder.textRegiones.typeface = mmedium
            holder.nombre.text = pokedex[position].pokemon_species.name.capitalize()
            holder.textRegiones.text = "Region $region"
            val number = getlastNumbers(pokedex[position].pokemon_species.url)
            util.fondoUrl(ctx,"${util.decrypt(util.PREFIJO_IMG)}$number.png",holder.imagen)

            holder.itemView.setOnClickListener {
                irOpcionesApp(DetailPokemon::class.java,number,region)
            }
            actualizaUIXNoSave(number.toInt(),holder.imgpokeBall)

            if(position == itemCount -1){
                holder.margen.visibility = View.VISIBLE
            }
        }

        fun actualizaUIXNoSave(id_sub_servicio: Int, imgpokeBall: ImageView){
            val selectedIndex = selectedPositions.indexOf(id_sub_servicio)
            if (selectedIndex > -1) {
                imgpokeBall.visibility = View.VISIBLE
            } else {
                imgpokeBall.visibility = View.GONE
            }
        }

        //funcion de ir a actividad sin cerrar home
        fun irOpcionesApp(act: Class<*>,dta1:String,dta2:String){
            val activity = ctx as Activity
            val intent = Intent(ctx, act)
            intent.putExtra("data1",dta1)
            intent.putExtra("data2",dta2)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.fade_in_trns, R.anim.fade_out_trns)
        }
        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    on_attach = false
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
            super.onAttachedToRecyclerView(recyclerView)
        }
        private var lastPosition = -1
        private var on_attach = true
        private fun setAnimation(view: View, position: Int) {
            if (position > lastPosition) {
                ItemAnimation.animate(view, if (on_attach) position else -1, animation_type)
                lastPosition = position
            }
        }

        fun getlastNumbers(number:String):String{
            var lastFourDigits = ""
            if (number.length > 4){
                lastFourDigits = number.substring(number.length - 8)
            } else {
                lastFourDigits = number
            }
            val p = Pattern.compile("[^0-9]")
            return p.matcher(lastFourDigits).replaceAll("")
        }
    }


}