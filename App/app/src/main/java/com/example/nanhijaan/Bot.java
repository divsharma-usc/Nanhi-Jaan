package com.example.nanhijaan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;

public class Bot extends AppCompatActivity {

    private ListView mListView;
    private FloatingActionButton mButtonSend;
    private EditText mEditTextMessage;
    private ImageView mImageView;
    private ChatMessageAdapter mAdapter;
    private JSONObject object;
    String[] queries;
    int id;
    String disease_name;
    String last_query, last_query_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot);
        mListView = (ListView) findViewById(R.id.listView);
        mButtonSend = (FloatingActionButton) findViewById(R.id.btn_send);
        mEditTextMessage = (EditText) findViewById(R.id.et_message);
        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);
        queries = new String[]{"Hello! How may I help you?", "Insufficient information. Please provide detailed symptoms.", "Do you want to read more about it?"};
        mimicOtherMessage(queries[0]);

//code for sending the message
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEditTextMessage.getText().toString();
                sendMessage(message);
                mEditTextMessage.setText("");
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });
    }

    private void sendMessage(String message) {
        last_query_parent = message;
        ChatMessage chatMessage = new ChatMessage(message, true);
        mAdapter.add(chatMessage);
        if(last_query.equals(queries[0]) || last_query.equals(queries[1])) {
            fetchDataFromServer(message);
        }
        else if(last_query.equals(queries[2])) {
            if(message.toLowerCase().equals("yes") || message.toLowerCase().equals("y")) {
                Intent intent = new Intent(Bot.this, DiseaseDetailsActivity.class);
                intent.putExtra("disease",disease_name);
                intent.putExtra("id", id);
                startActivity(intent);
            }
            else {
                mimicOtherMessage("Okay");
            }
        }
        else if(last_query_parent.toLowerCase().equals("bye")) {
            mimicOtherMessage("Bye! Hope to see you soon");
        }
        else {
            mimicOtherMessage("Okay");
        }
    }

    private void fetchDataFromServer(String query) {

        RequestQueue queue = Volley.newRequestQueue(this);

        final StringRequest request =
                new StringRequest(Request.Method.GET, UrlHelper.SIHAPI_GET_CHAT_RESPONSE + query,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    object = new JSONObject(response);
                                    int id = object.getInt("id");
                                    String disease_name = object.getString("name");
                                    showIdDiseaseChat(id, disease_name);
                                    Log.d("hey", "onResponse: "+response + id + disease_name);
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
                                Log.d("1234", "onErrorResponse: " + error);
                                Toast.makeText(Bot.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });

        queue.add(request);
    }

    private void showIdDiseaseChat(int id, String disease_name) {
        this.disease_name = disease_name;
        this.id = id;
        if(id == -1) {
            mimicOtherMessage(queries[1]);
        }
        else {
            mimicOtherMessage("Your child might be suffering from " + disease_name + " disease");
            mimicOtherMessage(queries[2]);
        }
//        mimicOtherMessage("");
    }

    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false);
        mAdapter.add(chatMessage);
        last_query = message;
    }
}
