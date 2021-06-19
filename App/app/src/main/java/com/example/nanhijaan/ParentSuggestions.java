package com.example.nanhijaan;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ParentSuggestions extends AppCompatActivity {

    TextView suggestions_tv;
    Button submit_btn;
    EditText suggestions_et;
    String language, titleStr, suggestions_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_suggestions);
        init();
        getLanguage();
        setTextViewLanguages();
        setListeners();
        ActionBar ab = getSupportActionBar();
        ab.setTitle(titleStr);
    }

    private void setListeners() {
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                suggestions_post = suggestions_et.getText().toString();
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(ParentSuggestions.this);
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("Suggestions", suggestions_post);
                    final String requestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlHelper.SIHAPI_POST_PARENTS_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("1234", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                Log.d("1234", "getBody: "+ requestBody);
                                Snackbar.make(v, "Your response has been recorded", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                suggestions_et.setText("");
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            String responseString = "";
                            if (response != null) {
                                responseString = String.valueOf(response.statusCode);
                                // can get more details such as response.headers
                            }
                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };

                    requestQueue.add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTextViewLanguages() {
        if(language == "hindi") {
            suggestions_tv.setText(getString(R.string.hindi_suggestions));
            submit_btn.setText(getString(R.string.hindi_submit));
        }
        else if(language == "punjabi") {
            suggestions_tv.setText(getString(R.string.punjabi_suggestions));
            submit_btn.setText(getString(R.string.punjabi_submit));
        }
        else if(language == "english") {
            suggestions_tv.setText(getString(R.string.eng_suggestions));
            submit_btn.setText(getString(R.string.eng_submit));
        }
    }

    private void getLanguage() {
        language = SetLanguage.getDefaults(SetLanguage.LANGUAGE, ParentSuggestions.this);
        if(language.equals("english"))
            titleStr = getString(R.string.eng_nanhijaan);
        else if(language.equals("hindi"))
            titleStr = getString(R.string.hindi_nanhijaan);
        else if(language.equals("punjabi"))
            titleStr = getString(R.string.punjabi_nanhijaan);
        Log.d("1234", "getLanguage: " + language);
    }


    private void init() {
        suggestions_tv = findViewById(R.id.suggestions_tv);
        suggestions_et = findViewById(R.id.suggestions_et);
        submit_btn = findViewById(R.id.suggestions_bt);
    }
}
