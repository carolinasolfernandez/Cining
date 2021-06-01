package com.csf.cining.helpers

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import java.util.*

@Suppress("DEPRECATION")
class LocaleManager {
    companion object {

        fun updateLocale(context: Context, language: String): Context {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return updateResources(context, language);
            }
            return updateResourcesLegacy(context, language);
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun updateResources(context: Context, language: String): Context {
            var context = context
            val res = context.resources
            val locale = Locale(language);
            Locale.setDefault(locale)

            val config = Configuration(res.configuration)

            config.setLocale(locale)
            context = context.createConfigurationContext(config)
            res.updateConfiguration(config, res.displayMetrics)

            return context;
        }

        fun setNewLocale(c: Context, language: String): Context {
            persistLanguage(c, language)
            return updateResources(c, language)
        }

        private fun persistLanguage(c: Context, language: String) {
            val prefs = getDefaultSharedPreferences(c)
            prefs.edit().putString("lang", language).commit()
        }

        @SuppressWarnings("deprecation")
        fun updateResourcesLegacy(context: Context, language: String): Context {
            val locale = Locale(language);
            Locale.setDefault(locale)

            val res = context.resources
            val config = res.configuration
            config.locale = locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLayoutDirection(locale);
            }
            res.updateConfiguration(config, res.displayMetrics);

            return context;
        }
    }
}