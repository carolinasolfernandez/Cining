package com.csf.cining.fragments.cines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csf.cining.R
import com.csf.cining.database.AppDatabase
import com.csf.cining.entities.Cine
import com.csf.cining.helpers.SwipeToDeleteCallback
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception
import kotlin.math.log


class ListFragment : Fragment() {

    private lateinit var v: View
    private lateinit var rec: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_cine_list, container, false)
        rec = v.findViewById(R.id.recycler_cine)

        rec.setHasFixedSize(true);
        rec.layoutManager = LinearLayoutManager(context)

        return v
    }

    override fun onStart() {
        super.onStart()
        //val db = AppDatabase.getAppDatabase(v.context)!!
        //val cineList = db.cineDao().getCines()
        var cineList: MutableList<Cine> = mutableListOf()
        FirebaseFirestore.getInstance().collection("cines").get()
            .addOnSuccessListener { result ->
                cineList = result.toObjects(Cine::class.java)
                val cineListAdapter = context?.let {
                    ListAdapter(cineList, it) { x ->
                        onItemClick(x.id)
                    }
                }!!
                rec.adapter = cineListAdapter

                val swipeHandler = object : SwipeToDeleteCallback(v.context) {
                    override fun onSwiped(h: RecyclerView.ViewHolder, dir: Int) {
                        cineListAdapter.removeAt(h.adapterPosition)
                    }
                }
                ItemTouchHelper(swipeHandler).attachToRecyclerView(rec)

                val newButton = v.findViewById<FloatingActionButton>(R.id.newCineButton)
                newButton.setOnClickListener() {
                    val action = ListFragmentDirections.actionCineListToCineCreate()
                    v.findNavController().navigate(action)
                }
            }
    }

    private fun onItemClick(aSub: String): Boolean {
        val action = ListFragmentDirections.actionCineListToCineDetail(aSub)
        v.findNavController().navigate(action)
        return true
    }
}
