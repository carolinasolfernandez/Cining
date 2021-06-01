package com.csf.cining.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.csf.cining.R
import com.csf.cining.helpers.LocaleManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleManager.updateLocale(
            baseContext,
            PreferenceManager.getDefaultSharedPreferences(baseContext).getString("lang", "")!!
        )
        setContentView(R.layout.activity_main)
    }
}