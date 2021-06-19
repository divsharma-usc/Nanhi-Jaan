package com.example.nanhijaan;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class Emergency extends AppCompatActivity {
    CardView doc1_cv,doc2_cv, doc3_cv,doc4_cv,doc5_cv,doc6_cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        init();
        add_listeners();
    }
    private void init() {
        doc1_cv = findViewById(R.id.doc1cv);
        doc2_cv = findViewById(R.id.doc2cv);
        doc3_cv = findViewById(R.id.doc3cv);
        doc4_cv = findViewById(R.id.doc4cv);
        doc5_cv = findViewById(R.id.doc5cv);
        doc6_cv = findViewById(R.id.doc6cv);



    }
    private void add_listeners() {
        doc1_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ "7018352301"));
                startActivity(intent);
            }
        });
        doc2_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ "7018352301"));
                startActivity(intent);
            }
        });
        doc3_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ "7018352301"));
                startActivity(intent);
            }
        });
        doc4_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ "7018352301"));
                startActivity(intent);
            }
        });
        doc5_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ "7018352301"));
                startActivity(intent);
              
            }
        });
        doc6_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ "7018352301"));
                startActivity(intent);
            }
        });
    }


}
