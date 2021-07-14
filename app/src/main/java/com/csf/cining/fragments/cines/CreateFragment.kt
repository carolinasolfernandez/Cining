package com.csf.cining.fragments.cines

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.csf.cining.R
import com.csf.cining.database.AppDatabase
import com.csf.cining.entities.Cine
import com.google.android.gms.tasks.Tasks
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.sql.Timestamp
import java.util.*


class CreateFragment : Fragment() {
    private val REQUEST_IMAGE_CAPTURE = 1
    lateinit var v: View

    private lateinit var name: EditText
    private lateinit var address: EditText
    private lateinit var website: EditText
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private lateinit var image: ImageView
    private lateinit var button: Button

    private lateinit var cloudDb: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cine_create, container, false)
        cloudDb = FirebaseFirestore.getInstance()
        return v
    }

    override fun onStart() {
        super.onStart()
        //var cine: Cine? = null

        val cineId = DetailFragmentArgs.fromBundle(requireArguments()).comCsfCiningEntitiesCineId

        name = v.findViewById(R.id.txtCineName)
        address = v.findViewById(R.id.txtCineAddress)
        website = v.findViewById(R.id.txtCineWebsite)
        latitude = v.findViewById(R.id.txtCineLatitude)
        longitude = v.findViewById(R.id.txtCineLongitude)
        button = v.findViewById(R.id.create_button)
        image = v.findViewById(R.id.image)

        image.setOnClickListener() {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                // display error state to the user
            }
        }

        if (cineId != "") {
            //val db = AppDatabase.getAppDatabase(v.context)!!
            //val cineDao = db.cineDao()
            //cine = cineDao.getCineById(cineId)
            edit(cineId)
        } else {
            create()
        }

        //val db = AppDatabase.getAppDatabase(v.context)!!
        //val cineDao = db.cineDao()


    }

    private fun edit(cineId: String) {
        cloudDb.collection("cines").document(cineId)
            .get().addOnSuccessListener { documentSnapshot ->
                documentSnapshot?.toObject(Cine::class.java)?.let { cine ->
                    run {
                        name.setText(cine.name)
                        address.setText(cine.address)
                        website.setText(cine.website)
                        latitude.setText("" + cine.latlng.latitude)
                        longitude.setText("" + cine.latlng.longitude)
                        Glide.with(v.context).load(cine.image).into(image);
                        image.tag = cine.image
                        if (cine.image != null && cine.image != "") {
                            image.setBackgroundResource(android.R.color.transparent)
                        }

                        button.setOnClickListener() {
                            if (checkFields()) {
                                var latitudeStr = latitude.text.toString()
                                var longitudeStr = longitude.text.toString()
                                if (latitudeStr == "" || longitudeStr == "") {
                                    latitudeStr = "0"
                                    longitudeStr = "0"
                                }
                                cine.name = name.text.toString()
                                cine.address = address.text.toString()
                                cine.website = website.text.toString()
                                cine.latlng =
                                    GeoPoint(latitudeStr.toDouble(), longitudeStr.toDouble())
                                cine.image = image.tag.toString()
                                //cineDao.updateCine(cine)
                                cloudDb.collection("cines").document(cine.id).set(cine)

                                Snackbar.make(v, "Cine ${name.text} saved!", Snackbar.LENGTH_LONG)
                                    .show()
                                name.text.clear()
                                address.text.clear()
                                website.text.clear()
                                latitude.text.clear()
                                longitude.text.clear()
                                image.tag = ""

                                val action = CreateFragmentDirections.actionCineCreateToCineList()
                                v.findNavController().navigate(action)
                            }
                        }
                    }
                }
            }
    }

    private fun create() {
        button.setOnClickListener() {
            if (checkFields()) {
                var latitudeStr = latitude.text.toString()
                var longitudeStr = longitude.text.toString()
                if (latitudeStr == "" || longitudeStr == "") {
                    latitudeStr = "0"
                    longitudeStr = "0"
                }

                val cineNew = Cine(
                    name.text.toString(),
                    address.text.toString(),
                    website.text.toString(),
                    GeoPoint(latitudeStr.toDouble(), longitudeStr.toDouble()),
                    image.tag?.toString()
                )
                cloudDb.collection("cines").add(cineNew)
                //cineDao.createCine(cineNew)
            }

            Snackbar.make(v, "Cine ${name.text} saved!", Snackbar.LENGTH_LONG).show()
            name.text.clear()
            address.text.clear()
            website.text.clear()
            latitude.text.clear()
            longitude.text.clear()
            image.tag = ""

            val action = CreateFragmentDirections.actionCineCreateToCineList()
            v.findNavController().navigate(action)
        }
    }

    private fun checkFields(): Boolean {
        return isComplete(name, getString(R.string.incomplete_name)) && (
                isComplete(
                    address,
                    getString(R.string.incomplete_address)
                ) || (
                        isComplete(
                            latitude,
                            getString(R.string.incomplete_coordinates)
                        ) &&
                                isComplete(
                                    longitude,
                                    getString(R.string.incomplete_coordinates)
                                )
                        )
                )
    }

    private fun isComplete(anEditText: EditText, anString: String): Boolean {
        if (TextUtils.isEmpty(anEditText.text.toString())) {
            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottom_bar)!!
            Snackbar.make(v, anString, Snackbar.LENGTH_LONG).apply {
                anchorView = bottomNavView
            }.show()
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            image.setImageBitmap(imageBitmap)
            image.setBackgroundResource(android.R.color.transparent)
            uploadImage()
        }
    }

    private fun uploadImage() {
        // Create a storage reference from our app
        val storageRef = Firebase.storage.reference.child(
            "images/${Timestamp(System.currentTimeMillis())}_${
                UUID.randomUUID().toString().substring(0, 2)
            }.jpg"
        )
        image.isDrawingCacheEnabled = true
        image.buildDrawingCache()
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT)
                    .show()
                returnTransition
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                image.tag = task.result;
                image.setImageBitmap(bitmap)
            }
        }
    }
}