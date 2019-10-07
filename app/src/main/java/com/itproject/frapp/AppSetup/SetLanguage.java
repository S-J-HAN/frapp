package com.itproject.frapp.AppSetup;

import android.content.Context;
import android.content.res.Configuration;
import java.util.Locale;



public class SetLanguage {


    public static void setLocale(Context context, String language) {
        Locale locale = Locale.ENGLISH;

        if (language.equals("en")) {
            locale = Locale.ENGLISH;
        }

        if (language.equals("zh")) {
            locale = Locale.CHINESE;
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

}
