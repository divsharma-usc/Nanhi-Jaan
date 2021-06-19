package com.example.nanhijaan;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Locale;

public class SetLanguage {

    public static final String LANGUAGE = "language" ;

 //   public static void setLocale(String language) {
//        if (language == "english"){
        //Locale.setDefault(Locale.ENGLISH);
//        //} else if (language == "hindi") {
//          //  Locale.setDefault(new Locale("hn"));
//        //} else if (language == "punjabi") {
//            //Locale.setDefault(new Locale("p"));
//        }
   // }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        //setLocale(value);

    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "english");
    }
}
