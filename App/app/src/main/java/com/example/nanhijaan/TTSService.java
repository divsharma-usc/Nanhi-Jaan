package com.example.nanhijaan;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TTSService implements TextToSpeech.OnInitListener {
    Context context;
    TextToSpeech tts;
    String language;

    public TTSService(Context c){
        context = c;
        tts = new TextToSpeech(context, this);
    }

    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            language = SetLanguage.getDefaults(SetLanguage.LANGUAGE, context);
            if (language == "hindi") {
                language = "hi";
            } else if (language == "english") {
                language = "en";
            } else if (language == "punjabi") {
                language = "pu";
            } else if (language == "bengali") {
                language = "bn";
            } else if (language == "tamil") {
                language = "ta";
            } else if (language == "telugu") {
                language = "te";
            }
            tts.setLanguage(new Locale(language));
            //tts.setLanguage(Locale.getDefault());
        }
    }

    public void say(String announcement) {
        tts.speak(announcement, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void stop() {
        tts.stop();
    }

    public void shutdown() {
        tts.shutdown();
    }
}
