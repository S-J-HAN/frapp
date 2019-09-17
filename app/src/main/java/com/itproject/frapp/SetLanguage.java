package com.itproject.frapp;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.core.content.ContextCompat;

import java.util.Locale;



// credit https://proandroiddev.com/change-language-programmatically-at-runtime-on-android-5e6bc15c758



public class SetLanguage {

    public static void setLanguage(Context c, String language) {
        setNewLocale(c, language);
    }

    public static void setNewLocale(Context c, String language) {
        persistLanguage(c, language);
        updateResources(c, language);
    }

    private static void persistLanguage(Context c, String language) {

    }

    private static void updateResources(Context c, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resource = c.getResources();
        Configuration config = new Configuration(resource.getConfiguration());
        config.locale = locale;

        resource.updateConfiguration(config, resource.getDisplayMetrics());
    }
}
