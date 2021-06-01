package com.csf.cining.fragments.cines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.csf.cining.R
import com.csf.cining.database.AppDatabase

class MapFragment : Fragment() {

    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_map, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val db = AppDatabase.getAppDatabase(v.context)!!
        db.cineDao().getCines().forEach {
            val latLng = LatLng(it.latitude, it.longitude)
            googleMap.addMarker(MarkerOptions().position(latLng).title(it.name))
        }

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(-34.5948882, -58.4467616),
                11.5f
            )
        )
    }
}