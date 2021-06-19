package com.example.nanhijaan;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nanhijaan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class DiseaseDetailsActivity extends AppCompatActivity {

    TextView introContent_tv, intro_tv, symptoms_tv, prevention_tv, food_tv, special_tv, physical_tv, mental_tv;
    FloatingActionButton fab;
    CardView symptoms_cv, prevention_cv, food_cv, special_cv, physical_cv, mental_cv;
    String language, general_info;
    JSONArray  special_needs, foods, physical_ex, mental_ex, symptoms, prevention;
    JSONObject object;
    String introStr, preventionStr, symptomsStr, foodStr, physicalStr, mentalStr, specialStr;
    String languageStr, contactStr, parentStr, diseaseStr;
    int disease_id;
    MenuItem languageItem, contactItem, parentItem;
    Menu menu;
    private TTSService tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_details);
        init();
        getLanguage();
        setTextViewLanguages();
        tts = new TTSService(getApplicationContext());
        Intent mIntent = getIntent();
        disease_id = mIntent.getIntExtra("id", 1);
        diseaseStr = mIntent.getStringExtra("disease");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(diseaseStr);
        fetchDataFromServer();
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

    private void setTextViewLanguages() {
        if(language.equals("hindi")) {
            Log.d("1234", "setTextViewLanguages: " + "hindi string set");
            introStr = getString(R.string.hindi_introduction);
            preventionStr = getString(R.string.hindi_prevention);
            symptomsStr = getString(R.string.hindi_symptoms);
            foodStr = getString(R.string.hindi_food);
            physicalStr = getString(R.string.hindi_physical);
            mentalStr = getString(R.string.hindi_mental);
            specialStr = getString(R.string.hindi_needs);
        }
        else if(language.equals("punjabi")) {
            introStr = getString(R.string.punjabi_introduction);
            preventionStr = getString(R.string.punjabi_prevention);
            symptomsStr = getString(R.string.punjabi_symptoms);
            foodStr = getString(R.string.punjabi_food);
            physicalStr = getString(R.string.punjabi_physical);
            mentalStr = getString(R.string.punjabi_mental);
            specialStr = getString(R.string.punjabi_needs);
        }
        else if(language.equals("english")) {
            introStr = getString(R.string.eng_introduction);
            preventionStr = getString(R.string.eng_prevention);
            symptomsStr = getString(R.string.eng_symptoms);
            foodStr = getString(R.string.eng_food);
            physicalStr = getString(R.string.eng_physical);
            mentalStr = getString(R.string.eng_mental);
            specialStr = getString(R.string.eng_needs);
        }
        else if(language.equals("bengali")) {
            introStr = getString(R.string.bengali_introduction);
            preventionStr = getString(R.string.bengali_prevention);
            symptomsStr = getString(R.string.bengali_symptoms);
            foodStr = getString(R.string.bengali_food);
            physicalStr = getString(R.string.bengali_physical);
            mentalStr = getString(R.string.bengali_mental);
            specialStr = getString(R.string.bengali_needs);
        }
        else if(language.equals("tamil")) {
            introStr = getString(R.string.tamil_introduction);
            preventionStr = getString(R.string.tamil_prevention);
            symptomsStr = getString(R.string.tamil_symptoms);
            foodStr = getString(R.string.tamil_food);
            physicalStr = getString(R.string.tamil_physical);
            mentalStr = getString(R.string.tamil_mental);
            specialStr = getString(R.string.tamil_needs);
        }
        else if(language.equals("telugu")) {
            introStr = getString(R.string.telugu_introduction);
            preventionStr = getString(R.string.telugu_prevention);
            symptomsStr = getString(R.string.telugu_symptoms);
            foodStr = getString(R.string.telugu_food);
            physicalStr = getString(R.string.telugu_physical);
            mentalStr = getString(R.string.telugu_mental);
            specialStr = getString(R.string.telugu_needs);
        }

        intro_tv.setText(introStr);
        prevention_tv.setText(preventionStr);
        symptoms_tv.setText(symptomsStr);
        food_tv.setText(foodStr);
        physical_tv.setText(physicalStr);
        mental_tv.setText(mentalStr);
        special_tv.setText(specialStr);
        Log.d("1234", "setTextViewLanguages: " + "hindi tv set");
    }

    private void fetchDataFromServer() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request =
                new StringRequest(Request.Method.GET, UrlHelper.SIHAPI_DISEASE_URL + disease_id,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                response= fixEncoding(response);
                                parseDiseaseDetailsJSON(response);
                                dialog.cancel();
                            }

                            private String fixEncoding(String response) {
                                try {
                                    byte[] u = response.toString().getBytes(
                                            "ISO-8859-1");
                                    response = new String(u, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                    return null;
                                }
                                return response;
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                dialog.cancel();
                                Log.d("1234", "onErrorResponse: " + error);
                                Toast.makeText(DiseaseDetailsActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });

        queue.add(request);
    }

    private void parseDiseaseDetailsJSON(String response) {
        try {
            object = new JSONObject(response);
            general_info = object.getString("general_info");
            symptoms = object.getJSONArray("symptoms");
            prevention = object.getJSONArray("preventions");
            foods = object.getJSONArray("foods");
            physical_ex = object.getJSONArray("physical_exercises");
            mental_ex = object.getJSONArray("mental_exercises");
            special_needs = object.getJSONArray("special_attentions");

            introContent_tv.setText(general_info);
            setListeners();

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void init() {
        fab = findViewById(R.id.fab);

        symptoms_cv = findViewById(R.id.symptomscv);
        prevention_cv = findViewById(R.id.preventioncv);
        food_cv = findViewById(R.id.foodcv);
        physical_cv = findViewById(R.id.physicalcv);
        mental_cv = findViewById(R.id.mentalcv);
        special_cv = findViewById(R.id.specialcv);
        introContent_tv = findViewById(R.id.intro_content);

        symptoms_tv = findViewById(R.id.symtomstv);
        intro_tv = findViewById(R.id.intro);
        prevention_tv = findViewById(R.id.preventiontv);
        food_tv = findViewById(R.id.foodtv);
        physical_tv = findViewById(R.id.physicaltv);
        mental_tv = findViewById(R.id.mentaltv);
        special_tv = findViewById(R.id.specialtv);
    }

    private void getLanguage() {
        language = SetLanguage.getDefaults(SetLanguage.LANGUAGE, DiseaseDetailsActivity.this);
        Log.d("1234", "getLanguage: " + language);
    }

    private void setListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder smf = new StringBuilder(introStr + ". " + introContent_tv.getText() + ". ");
                smf.append(symptomsStr + ". " + preventionStr + ". " + foodStr + ". " + physicalStr + ". " + mentalStr + ". " + specialStr);
                tts.say(smf.toString());
            }
        });
        setClickListener(symptoms_cv, "Symptoms");
        setClickListener(prevention_cv, "Prevention");
        setClickListener(food_cv, "Food");
        setClickListener(physical_cv, "Physical Activity");
        setClickListener(mental_cv, "Mental Activity");
        setClickListener(special_cv, "Special Needs");
    }

    private void setClickListener(CardView cv, final String title) {

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String heading = title;
                Intent i = new Intent(DiseaseDetailsActivity.this, SymptomsDetailsActivity.class);
                if(title == "Symptoms") {
                    String[] symptoms_str = new String[symptoms.length()];
                    convertJSONArrayToStringArray(symptoms, symptoms_str);
                    i.putExtra("content", symptoms_str);
                    i.putExtra("length", symptoms.length());
                    heading = symptomsStr;
                }
                else if(title == "Prevention") {
                    String[] preventions_str = new String[prevention.length()];
                    convertJSONArrayToStringArray(prevention, preventions_str);
                    i.putExtra("content", preventions_str);
                    i.putExtra("length", prevention.length());
                    heading = preventionStr;
                }
                else if(title == "Special Needs") {
                    String[] special_str = new String[special_needs.length()];
                    convertJSONArrayToStringArray(special_needs, special_str);
                    i.putExtra("content", special_str);
                    i.putExtra("length", special_needs.length());
                    heading = specialStr;
                }
                else if(title == "Food") {
                    String[] food_str = new String[foods.length()];
                    convertJSONArrayToStringArray(foods, food_str);
                    i.putExtra("content", food_str);
                    i.putExtra("length", foods.length());
                    heading = foodStr;
                }
                else if(title == "Physical Activity") {
                    String[] physical_str = new String[physical_ex.length()];
                    convertJSONArrayToStringArray(physical_ex, physical_str);
                    i.putExtra("content", physical_str);
                    i.putExtra("length", physical_ex.length());
                    heading = physicalStr;
                }
                else if(title == "Mental Activity") {
                    String[] mental_str = new String[mental_ex.length()];
                    convertJSONArrayToStringArray(mental_ex, mental_str);
                    i.putExtra("content", mental_str);
                    i.putExtra("length", mental_ex.length());
                    heading = mentalStr;
                }

                i.putExtra("disease", diseaseStr);
                i.putExtra("heading", heading);
                startActivity(i);
            }

            private void convertJSONArrayToStringArray(JSONArray JSONarray, String[] stringArray) {
                try {
                    for (int j = 0; j < JSONarray.length(); j++)
                        stringArray[j] = JSONarray.getString(j);
                }
                catch(JSONException e) {
                    Log.d("1234", "onClick: " + e);
                }
            }
        });
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
            Intent i = new Intent(DiseaseDetailsActivity.this, ParentSuggestions.class);
            startActivity(i);
        }
        else if(id == R.id.action_language) {
            Intent i = new Intent(DiseaseDetailsActivity.this, LanguageActivity.class);
            startActivity(i);
        }
        else if(id == R.id.action_contact) {
            Intent i = new Intent(DiseaseDetailsActivity.this, ContactUsActivity.class);
            startActivity(i);
        }
        else if(id == R.id.action_map) {
            Intent i = new Intent(DiseaseDetailsActivity.this, MapsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
