package com.csf.cining.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csf.cining.R
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.findNavController


class SplashFragment : Fragment() {
    private val splashTimeout: Long = 3000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_splash, container, false)

        Handler().postDelayed(
            {
                v.findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLogin())

            }, splashTimeout
        )
        return v
    }
}