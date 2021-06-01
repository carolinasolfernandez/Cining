package com.csf.cining.fragments.cines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.csf.cining.entities.Cine
import com.csf.cining.R
import com.csf.cining.database.AppDatabase

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
        cineList[position].name.let { holder.setName(it) }
        holder.getCardLayout().setOnClickListener {
            onItemClick(cineList[position])
        }
    }

    fun removeAt(cineId: Int) {
        val cine = cineList[cineId]
        cineList.removeAt(cineId)
        val db = AppDatabase.getAppDatabase(context)!!
        db.cineDao().deleteCine(cine)
        notifyItemRemoved(cineId)
    }

    override fun getItemCount(): Int {
        return cineList.size
    }

    class CineHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun setName(name: String) {
            val txt: TextView = view.findViewById(R.id.txt_name_item)
            txt.text = name
        }

        fun getCardLayout(): CardView {
            return view.findViewById(R.id.fragment_cine_list_item)
        }
    }
}