package com.csf.cining.fragments.cines

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import com.csf.cining.R
import com.csf.cining.database.AppDatabase
import com.csf.cining.entities.Cine
import com.google.android.material.snackbar.Snackbar


class CreateFragment : Fragment() {

    lateinit var v: View

    private lateinit var name: EditText
    private lateinit var address: EditText
    private lateinit var website: EditText
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cine_create, container, false)
        return v
    }

    override fun onStart() {
        super.onStart()
        var cine: Cine? = null

        val cineId = DetailFragmentArgs.fromBundle(requireArguments()).comCsfCiningEntitiesCineId
        if (cineId != 0) {
            val db = AppDatabase.getAppDatabase(v.context)!!
            val cineDao = db.cineDao()
            cine = cineDao.getCineById(cineId)
        }

        name = v.findViewById(R.id.txtCineName)
        address = v.findViewById(R.id.txtCineAddress)
        website = v.findViewById(R.id.txtCineWebsite)
        latitude = v.findViewById(R.id.txtCineLatitude)
        longitude = v.findViewById(R.id.txtCineLongitude)
        button = v.findViewById(R.id.create_button)

        if (cine != null) {
            name.setText(cine.name)
            address.setText(cine.address)
            website.setText(cine.website)
            latitude.setText("" + cine.latitude)
            longitude.setText("" + cine.longitude)

        }

        val db = AppDatabase.getAppDatabase(v.context)!!
        val cineDao = db.cineDao()

        button = v.findViewById(R.id.create_button)
        button.setOnClickListener() {
            if (isComplete(name, getString(R.string.incomplete_name)) && (
                        isComplete(address, getString(R.string.incomplete_address)) || (
                                isComplete(latitude, getString(R.string.incomplete_coordinates)) &&
                                        isComplete(longitude, getString(R.string.incomplete_coordinates))
                                )
                        )
            ) {
                var latitudeStr = latitude.text.toString()
                var longitudeStr = longitude.text.toString()
                if (latitudeStr == "" || longitudeStr == "") {
                    latitudeStr = "0"
                    longitudeStr = "0"
                }
                if (cine != null) {
                    cine.name = name.text.toString()
                    cine.address = address.text.toString()
                    cine.website = website.text.toString()
                    cine.latitude = latitudeStr.toDouble()
                    cine.longitude = longitudeStr.toDouble()
                    cineDao.updateCine(cine)
                } else {
                    cineDao.createCine(
                        Cine(
                            name.text.toString(),
                            address.text.toString(),
                            website.text.toString(),
                            latitudeStr.toDouble(),
                            longitudeStr.toDouble(),
                            ""
                        )
                    )
                }

                Snackbar.make(v, "Cine ${name.text} saved!", Snackbar.LENGTH_LONG).show()
                name.text.clear()
                address.text.clear()
                website.text.clear()
                latitude.text.clear()
                longitude.text.clear()

                val action = CreateFragmentDirections.actionCineCreateToCineList()
                v.findNavController().navigate(action)
            }
        }
    }

    private fun isComplete(anEditText: EditText, anString: String): Boolean {
        if (TextUtils.isEmpty(anEditText.text.toString())) {
            Snackbar.make(v, anString, Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }
}