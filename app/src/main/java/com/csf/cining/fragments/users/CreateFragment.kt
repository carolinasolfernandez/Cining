package com.csf.cining.fragments.users

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
import com.csf.cining.database.UserDao
import com.csf.cining.entities.User
import com.google.android.material.snackbar.Snackbar


class CreateFragment : Fragment() {

    private lateinit var v: View

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_user_create, container, false)
        return v
    }

    override fun onStart() {
        super.onStart()

        username = v.findViewById(R.id.txtUsername)
        password = v.findViewById(R.id.txtPw)
        email = v.findViewById(R.id.txtEmail)
        button = v.findViewById(R.id.register_button)

        db = AppDatabase.getAppDatabase(v.context)!!
        userDao = db.userDao()

        button.setOnClickListener {
            if (isComplete(username, getString(R.string.incomplete_username)) &&
                isComplete(email, getString(R.string.incomplete_email)) &&
                isComplete(password, getString(R.string.incomplete_password))
            ) {
                Snackbar.make(v, "User ${username.text} created!", Snackbar.LENGTH_LONG).show()
                userDao.insertPerson(User(username.text.toString(), password.text.toString()))

                username.text.clear()
                password.text.clear()
                email.text.clear()
                val action = CreateFragmentDirections.actionUserCreateToLogin()
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

