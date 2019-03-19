package com.victordev.pokegroup.Adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.victordev.pokegroup.ModelSerializado.Pokes
import com.victordev.pokegroup.R
import com.victordev.pokegroup.Views.GruposActivity
import com.victordev.pokegroup.utils.Utils


class AdapterListSectioned(
    context: Context,
    items: MutableList<Pokes>,
    funciones: Utils,
    dataReference: DatabaseReference,
    device: String
): RecyclerView.Adapter<RecyclerView.ViewHolder> (){


    private val VIEW_ITEM = 1
    private val VIEW_SECTION = 0

    private var items = items
    private var ctx: Context? = context
    private var func: Utils? = funciones
    private val device:String = device
    private var dataReference: DatabaseReference? = dataReference


    inner class OriginalViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var image: ImageView = v.findViewById<View>(R.id.image) as ImageView
        var name: TextView = v.findViewById<View>(R.id.name) as TextView
        var textType: TextView = v.findViewById<View>(R.id.textType) as TextView
        var textPeso: TextView = v.findViewById<View>(R.id.textPeso) as TextView
        var textTan: TextView = v.findViewById<View>(R.id.textTan) as TextView
    }

    class SectionViewHolder(v: View) : RecyclerView.ViewHolder(v) {

            var title_section = v.findViewById<View>(R.id.title_section) as TextView
            var edit = v.findViewById<View>(R.id.edit) as TextView
            var delete = v.findViewById<View>(R.id.delete) as TextView
            var title_shared = v.findViewById<View>(R.id.title_shared) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType == VIEW_ITEM) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_people_chat, parent, false)
            vh = OriginalViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_section, parent, false)
            vh = SectionViewHolder(v)
        }
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mmedium = Typeface.createFromAsset(ctx!!.assets, "font/MontserratMedium.ttf")
        val logox = Typeface.createFromAsset(ctx!!.assets, "font/Fredoka.ttf")

        val p = items[position]
        if (holder is OriginalViewHolder) {
            if(Utils.modeDark){
                holder.name.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
                holder.textPeso.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
                holder.textTan.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
                holder.textType.setTextColor(ContextCompat.getColor(ctx!!, R.color.blanco))
            }
            holder.name.typeface = mmedium
            holder.textPeso.typeface = mmedium
            holder.textTan.typeface = mmedium
            holder.textType.typeface = mmedium
            holder.name.text = p.names.capitalize()
            func!!.fondoUrl(ctx!!,p.image,holder.image)
            holder.textType.text = "${p.type}"
            holder.textPeso.text = "${p.weight}"
            holder.textTan.text = "${p.height}"
        } else {
            val view = holder as SectionViewHolder
            view.title_section.text = p.names.capitalize()
            view.title_section.typeface = logox
            view.edit.typeface = logox
            view.delete.typeface = logox
            view.delete.setOnClickListener {
                val ref = dataReference!!.child(device).orderByChild("id").equalTo(p.id)
               ref.addValueEventListener(object : ValueEventListener{
                   override fun onCancelled(p0: DatabaseError) {
                       println(p0)
                   }
                   override fun onDataChange(p0: DataSnapshot) {
                       if(p0.exists()){
                            for (data in p0.children){
                                data.ref.removeValue()
                            }
                       }
                   }
               })
            }
            view.title_section.setOnClickListener {
                (ctx as GruposActivity).dialogoNombre(p.id,p.names)
            }
            view.edit.setOnClickListener {
                (ctx as GruposActivity).editarGrupo(p.id,p.url,p.region,p.names)
            }
            view.title_shared.setOnClickListener {
                (ctx as GruposActivity).onShareClicked(p.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (this.items[position].section) VIEW_SECTION else VIEW_ITEM
    }


}