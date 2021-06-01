package com.csf.cining.fragments.cines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import com.csf.cining.R
import com.csf.cining.database.AppDatabase
import com.csf.cining.entities.Cine
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.snackbar.Snackbar


class DetailFragment : Fragment() {
    lateinit var v: View
    lateinit var c: Cine

    private lateinit var cineName: TextView
    private lateinit var cineAddress: TextView
    private lateinit var cineWebsite: TextView
    private lateinit var cineLatitude: TextView
    private lateinit var cineLongitude: TextView
    private lateinit var button: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cine_detail, container, false)
        cineName = v.findViewById(R.id.cineName)
        cineAddress = v.findViewById(R.id.cineAddress)
        cineWebsite = v.findViewById(R.id.cineWebsite)
        cineLatitude = v.findViewById(R.id.cineLatitude)
        cineLongitude = v.findViewById(R.id.cineLongitude)

        return v
    }


    override fun onStart() {
        super.onStart()
        val cineId = DetailFragmentArgs.fromBundle(requireArguments()).comCsfCiningEntitiesCineId

        val db = AppDatabase.getAppDatabase(v.context)!!
        val cine = db.cineDao().getCineById(cineId)
        if (cine != null) {
            c = cine
            cineName.text = cine.name
            cineAddress.text = cine.address
            cineWebsite.text = cine.website
            cineLatitude.text = cine.latitude.toString()
            cineLongitude.text = cine.longitude.toString()
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.cineMap) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        }

        button = v.findViewById(R.id.edit_button)
        button.setOnClickListener() {
                val action = DetailFragmentDirections.actionCineDetailToCineEdit(c.id)
                v.findNavController().navigate(action)
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val latLng = LatLng(c.latitude, c.longitude)
        googleMap.addMarker(MarkerOptions().position(latLng).title(c.name))
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(c.latitude,  c.longitude),
                16f
            )
        )
    }
}
