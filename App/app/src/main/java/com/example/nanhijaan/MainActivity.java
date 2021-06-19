package com.example.nanhijaan;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements DiseaseAdapter.ItemClickListener {

    private Context mContext;
    Toolbar toolbar;

    FloatingActionButton fab, chat_fab, emergency_fab;
    RelativeLayout disease_rl;
    int num_diseases, disease_IDs[];
    Vector<String> disease_names;
    CardView disease_cv[];
    ImageButton search_ib;
    String language, languageStr, contactStr, parentStr, titleStr, mapStr;
    EditText search_et;
    JSONObject object;
    MenuItem languageItem, contactItem, parentItem, mapItem, reminderItem, pillItem, historyItem;
    Menu menu;
    private RecyclerView recyclerView;
    private DiseaseAdapter adapter;
    private List<Disease> diseaseList;
    int[] myImageList;

    public static final int RECORD_AUDIO = 0;
    final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

    final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

    private TTSService tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        init();
        getLanguage();
        search_et.setFocusable(false);
        myImageList = new int[]{R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5, R.drawable.img6};
        fetchDataFromServer();

        tts = new TTSService(mContext);

        String lang = SetLanguage.getDefaults(SetLanguage.LANGUAGE, mContext), l = "en";
        if (lang.equals("hindi")) {
            l = "hi";
        } else if (lang.equals("english")) {
            l = "en";
        } else if (lang.equals("punjabi")) {
            l = "pu";
        } else if (lang.equals("bengali")) {
            l = "bn";
        } else if (lang.equals("tamil")) {
            l = "ta";
        } else if (lang.equals("telugu")) {
            l = "te";
        }

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, new Locale(l));

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null) {
                    search_for_disease(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(titleStr);
    }

    private void setMenuLanguages() {
        if(language.equals("hindi")) {
            parentStr = getString(R.string.hindi_suggestions);
            contactStr = getString(R.string.hindi_contact);
            languageStr = getString(R.string.hindi_language);
            mapStr = "आसपास के अस्पताल";

        }
        else if(language.equals("punjabi")) {
            parentStr = getString(R.string.punjabi_suggestions);
            contactStr = getString(R.string.punjabi_contact);
            languageStr = getString(R.string.punjabi_language);
            mapStr = "ਨੇੜਲੇ ਹਸਪਤਾਲਾਂ";
        }
        else if(language.equals("english")) {
            parentStr = getString(R.string.eng_suggestions);
            contactStr = getString(R.string.eng_contact);
            languageStr = getString(R.string.eng_language);
            mapStr = "Nearby Hospitals";
        }
       else if(language.equals("bengali")) {
            parentStr = getString(R.string.bengali_suggestions);
            contactStr = getString(R.string.bengali_contact);
            languageStr = getString(R.string.bengali_language);
            mapStr = "কাছাকাছি হাসপাতাল";
        }
        else if(language.equals("tamil")) {
            parentStr = getString(R.string.tamil_suggestions);
            contactStr = getString(R.string.tamil_contact);
            languageStr = getString(R.string.tamil_language);
            mapStr = "அருகிலுள்ள மருத்துவமனைகள்";
        }
        else if(language.equals("telugu")) {
            parentStr = getString(R.string.telugu_suggestions);
            contactStr = getString(R.string.telugu_contact);
            languageStr = getString(R.string.telugu_language);
            mapStr = "సమీపంలోని ఆసుపత్రులు";
        }
        languageItem.setTitle(languageStr);
        contactItem.setTitle(contactStr);
        parentItem.setTitle(parentStr);
        mapItem.setTitle(mapStr);
    }

    private void getLanguage() {
        language = SetLanguage.getDefaults(SetLanguage.LANGUAGE, MainActivity.this);
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

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        emergency_fab = findViewById(R.id.emergency_fab);
//        disease_rl = findViewById(R.id.cards_rl);
        search_ib = findViewById(R.id.searchib);
        search_et = findViewById(R.id.searchet);
        chat_fab = findViewById(R.id.chat_fab);
        search_et.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void fetchDataFromServer() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        final StringRequest request =
                new StringRequest(Request.Method.GET, UrlHelper.SIHAPI_URL + language,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                response= fixEncoding(response);

                                parseDiseaseJSON(response);
                                Log.d("1234", "onResponse: " + response);
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
                                Toast.makeText(MainActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });

        queue.add(request);
    }

    private void parseDiseaseJSON(String response) {
        try {
            object = new JSONObject(response);
            num_diseases = object.getInt("length");
            JSONArray disease_json_array = object.getJSONArray("data");
            Log.d("1234", "num_diseases: " + num_diseases);

            disease_IDs = new int[num_diseases];
            disease_names=new Vector<>(num_diseases);

            for(int i=0; i<disease_json_array.length(); i++) {
                disease_names.add(disease_json_array.getJSONObject(i).getString("name"));
                disease_IDs[i] = disease_json_array.getJSONObject(i).getInt("object_id");
                Log.d("1234", "disease names: " + disease_names.get(i) + " " + i);
            }
            placeCards(num_diseases);
            setListener();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void search_for_disease(String input_text){
        //Toast.makeText(mContext, input_text, Toast.LENGTH_SHORT).show();
        input_text = input_text.toLowerCase();
        for(int i=0;i<disease_names.size();i++){
            //Toast.makeText(mContext, "searched for "+disease_names.get(i), Toast.LENGTH_SHORT).show();
            if(input_text.contains(disease_names.get(i).toLowerCase())) {
                Intent intent = new Intent(MainActivity.this, DiseaseDetailsActivity.class);
                intent.putExtra("disease",disease_names.get(i));
                intent.putExtra("id", disease_IDs[i]);
                startActivity(intent);
                return;
            }
        }
        Toast.makeText(getApplicationContext(), "No search results found! ", Toast.LENGTH_SHORT).show();
    }

    private void placeCards(int num_diseases) {
//        RelativeLayout.LayoutParams relParams[] = new RelativeLayout.LayoutParams[num_diseases];
//        disease_cv = new CardView[num_diseases];
//
//        for(int i=0; i<num_diseases; i++) {
//            disease_cv[i] = new CardView(mContext);
//            disease_cv[i].setCardBackgroundColor(Color.parseColor("#FDFDFD"));
//            disease_cv[i].setCardElevation(4);
//            disease_cv[i].setId(i+1);
//            disease_cv[i].setRadius(10);
//
//
//            relParams[i] = new RelativeLayout.LayoutParams(500, 500);
//            relParams[i].setMargins(10, 10, 10, 10);
//            if(i%2 == 1) {
//                relParams[i].addRule(RelativeLayout.RIGHT_OF, disease_cv[i-1].getId());
//                if(i != 1) {
//                    relParams[i].addRule(RelativeLayout.BELOW, disease_cv[i-2].getId());
//                }
//            }
//            else if(i%2 == 0 && i != 0) {
//                relParams[i].addRule(RelativeLayout.BELOW, disease_cv[i-2].getId());
//            }
//
//
//            //            Put ImageView on CardView
//            ImageView iv = new ImageView(mContext);
//            iv.setId(i + 10000);
//            iv.setImageResource(R.mipmap.ic_launcher);  // TODO: From API
//            RelativeLayout.LayoutParams ivparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            ivparams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//            ivparams.setMargins(150,150,100,100);
//            iv.setLayoutParams(ivparams);
//
//
//            //            Put the TextView in CardView
//            TextView tv = new TextView(mContext);
//            RelativeLayout.LayoutParams tvparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            tvparams.addRule(RelativeLayout.BELOW, iv.getId());
//            tv.setText(disease_names.get(i));
//            tvparams.setMargins(150,350,100,100); // TODO: NEED TO CHANGE... SHOULD BE BELOW IMAGEVIEW
//            //tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
//            tv.setTextColor(Color.BLACK);
//            tv.setLayoutParams(tvparams);
//
//
//            // Finally, add the CardView in root layout
//            disease_cv[i].addView(iv, ivparams);
//            disease_cv[i].addView(tv, tvparams);
//            disease_rl.addView(disease_cv[i], relParams[i]);
//        }
        recyclerView = findViewById(R.id.recycler_view);

        diseaseList = new ArrayList<>();

        int it_images = 0;
        for(int i=0; i<num_diseases; i++) {
            Disease ds = new Disease(disease_names.get(i), myImageList[it_images]);
            diseaseList.add(ds);
            if(it_images>=5)

                it_images = 0;
            else
                it_images++;
        }
        adapter = new DiseaseAdapter(this, diseaseList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        adapter.setClickListener(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this, DiseaseDetailsActivity.class);
        intent.putExtra("disease",disease_names.get(position));
        intent.putExtra("id", disease_IDs[position]);
        startActivity(intent);
    }


    private void setListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder diseases = new StringBuilder();
                for(String dn:disease_names){
                    diseases.append(dn + ". ");
                }
                tts.say(diseases.toString());
            }
        });


        chat_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Bot.class);
                startActivity(intent);
            }
        });

        emergency_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Emergency.class);
                startActivity(intent);
            }
        });


        search_ib.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    float x = (float) 2;
                    float y = (float) 2;

                    search_ib.setScaleX(x);
                    search_ib.setScaleY(y);
                    //search_ib.setBackgroundResource(R.drawable.blue_220);

                    AppLog.logString("Start Recording");
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);

                    } else{
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    }
                    return true;
                }

                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    float x = 1;
                    float y = 1;

                    search_ib.setScaleX(x);
                    search_ib.setScaleY(y);

                    mSpeechRecognizer.stopListening();
                }
                return false;
            }
        });

//        for(int i=0; i<num_diseases; i++) {
//            final int index = i;
//            disease_cv[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(MainActivity.this, DiseaseDetailsActivity.class);
//                    intent.putExtra("disease",disease_names.get(index));
//                    intent.putExtra("id", disease_IDs[index]);
//                    startActivity(intent);
//                }
//            });
//        }

        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search_for_disease(search_et.getText().toString());

                    return true;
                }
                return false;
            }
        });

        search_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                search_et.setFocusableInTouchMode(true);
                search_et.clearFocus();
                return false;
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
        mapItem = menu.findItem(R.id.action_map);
        reminderItem = menu.findItem(R.id.action_reminder);
        pillItem = menu.findItem(R.id.action_pills);
        historyItem = menu.findItem(R.id.action_history);
        setMenuLanguages();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_parent) {
            Intent i = new Intent(MainActivity.this, ParentSuggestions.class);
            startActivity(i);
        }
        else if(id == R.id.action_language) {
            Intent i = new Intent(MainActivity.this, LanguageActivity.class);
            startActivity(i);
        }
        else if(id == R.id.action_contact) {
            Intent i = new Intent(MainActivity.this, ContactUsActivity.class);
            startActivity(i);
        }
        else if(id == R.id.action_map) {
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(i);
        }
        else if(id == R.id.action_reminder) {
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("allDay", true);
            intent.putExtra("rrule", "FREQ=YEARLY");
            intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
            intent.putExtra("title", "Title");
            startActivity(intent);
        }
        else if(id == R.id.action_pills) {
            // Create a Uri from an intent string. Use the result to create an Intent.
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=pharmacy");

// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
// Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps");

// Attempt to start an activity that can handle the Intent
            startActivity(mapIntent);

        }
        else if(id == R.id.action_history) {
            Intent i = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }
}
