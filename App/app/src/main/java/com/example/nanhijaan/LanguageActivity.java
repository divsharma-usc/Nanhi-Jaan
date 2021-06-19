package com.example.nanhijaan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class LanguageActivity extends AppCompatActivity {

    CardView eng_cv, hindi_cv, punjabi_cv,bengali_cv,tamil_cv,telugu_cv;
    TextView choose_tv;
    String language, titleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        init();
        getLanguage();
        setTextViewLanguages();
        add_listeners();

        ActionBar ab = getSupportActionBar();
        ab.setTitle(titleStr);
    }

    private void setTextViewLanguages() {
        if(language.equals("hindi")) {
            choose_tv.setText(getString(R.string.hindi_choose_language));
        }
        else if(language.equals("punjabi")) {
            choose_tv.setText(getString(R.string.punjabi_choose_language));
        }
        else if(language.equals("english")){
            choose_tv.setText(getString(R.string.eng_choose_language));
        }
        else if(language.equals("bengali")){
            choose_tv.setText(getString(R.string.bengali_choose_language));
        }
        else if(language.equals("tamil")){
            choose_tv.setText(getString(R.string.tamil_choose_language));
        }
        else if(language.equals("telugu")){
            choose_tv.setText(getString(R.string.telugu_choose_language));
        }

    }

    private void add_listeners() {
        set_language(hindi_cv, "hindi");
        set_language(eng_cv, "english");
        set_language(punjabi_cv, "punjabi");
        set_language(bengali_cv, "bengali");
        set_language(tamil_cv, "tamil");
        set_language(telugu_cv, "telugu");
    }

    private void getLanguage() {
        language = SetLanguage.getDefaults(SetLanguage.LANGUAGE, LanguageActivity.this);
        if(language.equals("english"))
            titleStr = getString(R.string.eng_nanhijaan);
        else if(language.equals("hindi"))
            titleStr = getString(R.string.hindi_nanhijaan);
        else if(language.equals("punjabi"))
            titleStr = getString(R.string.punjabi_nanhijaan);
        else if(language.equals("bengali"))
            titleStr = getString(R.string.bengali_nanhijaan);
        else if(language.equals("tamil"))
            titleStr = getString(R.string.tamil_nanhijaan);
        else if(language.equals("telugu"))
            titleStr = getString(R.string.telugu_nanhijaan);
        Log.d("1234", "getLanguage: " + language);
    }

    private void set_language(CardView cv, final String language) {
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetLanguage.setDefaults(SetLanguage.LANGUAGE, language, LanguageActivity.this);
                Intent i = new Intent(LanguageActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    private void init() {
        eng_cv = findViewById(R.id.englishcv);
        hindi_cv = findViewById(R.id.hindicv);
        punjabi_cv = findViewById(R.id.punjabicv);
        bengali_cv = findViewById(R.id.bengalicv);
        tamil_cv = findViewById(R.id.tamilcv);
        telugu_cv = findViewById(R.id.telugucv);


        choose_tv = findViewById(R.id.language_tv);
    }
}
