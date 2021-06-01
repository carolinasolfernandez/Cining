package com.csf.cining.fragments.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.findNavController
import com.csf.cining.R
import com.csf.cining.database.AppDatabase
import com.csf.cining.database.UserDao
import com.csf.cining.entities.User
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {
    lateinit var v: View
    lateinit var userDao: UserDao

    lateinit var usernameDataInput: EditText
    lateinit var passDataInput: EditText
    lateinit var loginButton: Button
    lateinit var signUp: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false)
        usernameDataInput = v.findViewById(R.id.txtUsername)
        passDataInput = v.findViewById(R.id.editTextTextPassword)
        loginButton = v.findViewById(R.id.buttonLogin)
        signUp = v.findViewById(R.id.sign_up)
        return v
    }

    companion object {}

    override fun onStart() {
        super.onStart()
        val db = AppDatabase.getAppDatabase(v.context)!!
        userDao = db.userDao()

        //userDao.insertPerson(User("caro", "caro"))

        loginButton.setOnClickListener {
            val user = verifyUser(usernameDataInput.text.toString(), passDataInput.text.toString())
            if (user != null) {
                Snackbar.make(v, "Hi ${user.username}!", Snackbar.LENGTH_LONG).show()
                val action = ActionOnlyNavDirections(R.id.action_login_to_listActivity)
                v.findNavController().navigate(action)
            } else {
                usernameDataInput.text.clear()
                passDataInput.text.clear()
                Snackbar.make(
                    v,
                    getString(R.string.sign_up_incorrect),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        signUp.setOnClickListener {
            val action = ActionOnlyNavDirections(R.id.action_login_to_userCreate)
            v.findNavController().navigate(action)
        }
    }

    private fun verifyUser(username: String, password: String): User? {
        val users = userDao.getUsers() as MutableList<User>
        for (user in users) {
            if (user.verifyUser(username, password)) {
                return user
            }
        }
        return null
    }
}