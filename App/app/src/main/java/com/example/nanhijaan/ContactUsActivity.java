package com.example.nanhijaan;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ContactUsActivity extends AppCompatActivity {

    TextView contact_tv;
    String language, titleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        language = SetLanguage.getDefaults(SetLanguage.LANGUAGE, ContactUsActivity.this);
        contact_tv = findViewById(R.id.contact_tv);
        if(language.equals("hindi")) {
            contact_tv.setText(getString(R.string.hindi_contact));
            titleStr = getString(R.string.hindi_nanhijaan);
        }
        else if(language.equals("punjabi")) {
            contact_tv.setText(getString(R.string.punjabi_contact));
            titleStr = getString(R.string.punjabi_nanhijaan);
        }
        else if(language.equals("english")) {
            contact_tv.setText(getString(R.string.eng_contact));
            titleStr = getString(R.string.eng_nanhijaan);
        }
        else if(language.equals("bengali")) {
            contact_tv.setText(getString(R.string.bengali_contact));
            titleStr = getString(R.string.bengali_nanhijaan);
        }
        else if(language.equals("tamil")) {
            contact_tv.setText(getString(R.string.tamil_contact));
            titleStr = getString(R.string.tamil_nanhijaan);
        }
        else if(language.equals("telugu")) {
            contact_tv.setText(getString(R.string.telugu_contact));
            titleStr = getString(R.string.telugu_nanhijaan);
        }



        ActionBar ab = getSupportActionBar();
        ab.setTitle(titleStr);
    }
}
