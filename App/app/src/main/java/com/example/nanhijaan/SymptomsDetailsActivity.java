package com.example.nanhijaan;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SymptomsDetailsActivity extends AppCompatActivity {

    String heading, content[], details = "";
    TextView heading_tv, content_tv;
    Menu menu;
    String language, diseaseStr;
    String languageStr, contactStr, parentStr;
    MenuItem languageItem, contactItem, parentItem;
    FloatingActionButton fab;
    private TTSService tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms_details);
        init();
        getLanguage();
        tts = new TTSService(getApplicationContext());
        Intent mIntent = getIntent();
        heading = mIntent.getStringExtra("heading");
        heading_tv.setText(heading);
        int length = mIntent.getIntExtra("length", 0);
        content = mIntent.getStringArrayExtra("content");
        diseaseStr = mIntent.getStringExtra("disease");

        for(int i=0; i<length; i++) {
            details = details + content[i] + "\n";
        }
        content_tv.setText(details);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder smf = new StringBuilder(heading + ". ");
                for(String ct:content){
                    smf.append(ct + ". ");
                }
                tts.say(smf.toString());
            }
        });
        ActionBar ab = getSupportActionBar();
        ab.setTitle(diseaseStr);
    }

    private void getLanguage() {
        language = SetLanguage.getDefaults(SetLanguage.LANGUAGE, SymptomsDetailsActivity.this);
        Log.d("1234", "getLanguage: " + language);
    }

    private void init() {
        heading_tv = findViewById(R.id.intro);
        content_tv = findViewById(R.id.intro_content);
        fab = findViewById(R.id.fab);
    }

    private void setMenuLanguages() {
        if(language.equals("hindi")) {
            parentStr = getString(R.string.hindi_suggestions);
            contactStr = getString(R.string.hindi_contact);
            languageStr = getString(R.string.hindi_language);
        }
        else if(language.equals("punjabi")) {
            parentStr = getString(R.string.punjabi_suggestions);
            contactStr = getString(R.string.punjabi_contact);
            languageStr = getString(R.string.punjabi_language);
        }
        else if(language.equals("english")) {
            parentStr = getString(R.string.eng_suggestions);
            contactStr = getString(R.string.eng_contact);
            languageStr = getString(R.string.eng_language);
        }
        else if(language.equals("bengali")) {
            parentStr = getString(R.string.bengali_suggestions);
            contactStr = getString(R.string.bengali_contact);
            languageStr = getString(R.string.bengali_language);
        }
        else if(language.equals("tamil")) {
            parentStr = getString(R.string.tamil_suggestions);
            contactStr = getString(R.string.tamil_contact);
            languageStr = getString(R.string.tamil_language);
        }
        else if(language.equals("telugu")) {
            parentStr = getString(R.string.telugu_suggestions);
            contactStr = getString(R.string.telugu_contact);
            languageStr = getString(R.string.telugu_language);
        }
        languageItem.setTitle(languageStr);
        contactItem.setTitle(contactStr);
        parentItem.setTitle(parentStr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        parentItem = menu.findItem(R.id.action_parent);
        contactItem = menu.findItem(R.id.action_contact);
        languageItem = menu.findItem(R.id.action_language);
        setMenuLanguages();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        tts.stop();
        //tts.shutdown();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_parent) {
            Intent i = new Intent(SymptomsDetailsActivity.this, ParentSuggestions.class);
            startActivity(i);
        }
        else if(id == R.id.action_language) {
            Intent i = new Intent(SymptomsDetailsActivity.this, LanguageActivity.class);
            startActivity(i);
        }
        else if(id == R.id.action_contact) {
            Intent i = new Intent(SymptomsDetailsActivity.this, ContactUsActivity.class);
            startActivity(i);
        }
        else if(id == R.id.action_map) {
            Intent i = new Intent(SymptomsDetailsActivity.this, MapsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
