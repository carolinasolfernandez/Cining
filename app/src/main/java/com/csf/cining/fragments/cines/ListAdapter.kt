package com.csf.cining.fragments.cines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csf.cining.R
import com.csf.cining.database.AppDatabase
import com.csf.cining.entities.Cine
import com.google.firebase.firestore.FirebaseFirestore


class ListAdapter(
    private var cineList: MutableList<Cine>,
    private val context: Context,
    val onItemClick: (Cine) -> Boolean
) : RecyclerView.Adapter<ListAdapter.CineHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CineHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_cine_list_item, parent, false)
        return (CineHolder(view))
    }

    override fun onBindViewHolder(holder: CineHolder, position: Int) {
        cineList[position].let {
            holder.setName(it)
        }
        holder.getCardLayout().setOnClickListener {
            onItemClick(cineList[position])
        }
    }

    fun removeAt(cineId: Int) {
        val cine = cineList[cineId]
        cineList.removeAt(cineId)
        FirebaseFirestore.getInstance().collection("cines").document(cine.id).delete()

        //val db = AppDatabase.getAppDatabase(context)!!
        //db.cineDao().deleteCine(cine)
        notifyItemRemoved(cineId)
    }

    override fun getItemCount(): Int {
        return cineList.size
    }

    class CineHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun setName(cine: Cine) {
            val txt: TextView = view.findViewById(R.id.txt_name_item)
            val img: ImageView = view.findViewById(R.id.image_view)
            txt.text = cine.name
            var url = cine.image
            if (url == null || url == ""){
                url = "https://inmobiliare.com/himalaya/wp-content/uploads/2020/08/C%C3%B3mo-ser%C3%A1-la-nueva-normalidad-en-los-cines-inmobiliare.jpg"
            }
            Glide.with(view.context).load(url).into(img);
        }

        fun getCardLayout(): CardView {
            return view.findViewById(R.id.fragment_cine_list_item)
        }
    }
}