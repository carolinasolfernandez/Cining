package com.csf.cining.fragments.users

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.csf.cining.R
import com.csf.cining.database.AppDatabase
import com.csf.cining.database.UserDao
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class CreateFragment : Fragment() {

    private lateinit var v: View

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

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
        password = v.findViewById(R.id.txtPw)
        email = v.findViewById(R.id.txtEmail)
        button = v.findViewById(R.id.register_button)

        //db = AppDatabase.getAppDatabase(v.context)!!
        //userDao = db.userDao()

        button.setOnClickListener {
            if (isComplete(email, getString(R.string.incomplete_email)) &&
                isValidEmail(email, getString(R.string.invalid_email)) &&
                isComplete(password, getString(R.string.incomplete_password)) &&
                isSecure(password, getString(R.string.short_password))
            ) {
                Snackbar.make(v, "User ${email.text} created!", Snackbar.LENGTH_LONG).show()
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            password.text.clear()
                            email.text.clear()
                            val action = CreateFragmentDirections.actionUserCreateToLogin()
                            v.findNavController().navigate(action)
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(
                                v,
                                getString(R.string.auth_failed),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }

                    }
                //userDao.insertPerson(User(email.text.toString(), password.text.toString()))
/*
                password.text.clear()
                email.text.clear()
                val action = CreateFragmentDirections.actionUserCreateToLogin()
                v.findNavController().navigate(action)
 */
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

    private fun isValidEmail(anEditText: EditText, anString: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(anEditText.text.toString()).matches()) {
            Snackbar.make(v, anString, Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun isSecure(anEditText: EditText, anString: String): Boolean {
        if (anEditText.text.toString().length < 6) {
            Snackbar.make(v, anString, Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }
}

