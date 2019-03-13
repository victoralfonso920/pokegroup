package com.victordev.pokegroup.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.muddzdev.styleabletoastlibrary.StyleableToast
import com.victordev.pokegroup.ModelSerializado.Result
import com.victordev.pokegroup.R
import com.victordev.pokegroup.Views.DetailRegion
import com.victordev.pokegroup.Views.HomeActivity
import com.victordev.pokegroup.utils.Utils


class RecyclerRegionesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



    //instancia de controles
    val ImgRegion: ImageView = itemView.findViewById(R.id.ImgRegion)
    val linReg: LinearLayout = itemView.findViewById(R.id.linReg)
    val textNombre: TextView = itemView.findViewById(R.id.textNombre)



    //adapters
    class RecyclerRegionesAdapter(private val regiones: List<Result>, private val ctx: Context)
        : RecyclerView.Adapter<RecyclerRegionesHolder>() {

        var morgab: Typeface? = null
        var bebasBold: Typeface? = null
        var robotoreg: Typeface? = null
        private val inflater: LayoutInflater = LayoutInflater.from(ctx)
        private val util: Utils = Utils()

        //se obtiene tamaño de dato segun tipo
        override fun getItemCount(): Int {
                return regiones.size
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerRegionesHolder {
            val itemView = inflater.inflate(R.layout.item_regiones, parent, false)
            return RecyclerRegionesHolder(itemView)

        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerRegionesHolder, position: Int) {
            holder.setIsRecyclable(false)

            //fuentes
            val logox = Typeface.createFromAsset(ctx.assets, "font/Fredoka.ttf")
            holder.textNombre.typeface = logox
            holder.textNombre.text = "Region ${regiones[position].name.capitalize()}"
            //agregar fotos
            val resourceId:Int = ctx.resources.getIdentifier(regiones[position].name, "drawable", ctx.packageName)
            if(resourceId > 0){
                holder.ImgRegion.visibility = View.VISIBLE
                util.fondo(ctx,resourceId,holder.ImgRegion)
            }else{
                holder.ImgRegion.visibility = View.GONE
               holder.linReg.visibility = View.VISIBLE
            }

            holder.itemView.setOnClickListener {
                (ctx as HomeActivity).irOpcionesApp(DetailRegion::class.java,regiones[position].name,regiones[position].url)
            }


        }


        //mostrar toast de error
        fun showToastError(mensaje: String, drawable: Int) {
            var st: StyleableToast = StyleableToast.Builder(ctx)
                    .text(mensaje)
                    .textColor(Color.WHITE)
                    .backgroundColor(ContextCompat.getColor(ctx, R.color.Red))
                    .icon(drawable)
                    .build()
            st.show()
        }
    }


}