package com.example.gptproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        String response = intent.getStringExtra("response");
        TextView gpt = findViewById(R.id.textViewGPT);
        gpt.setText(response);
        Button redo = findViewById(R.id.redo);
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity2.this, MainActivity.class);
                myIntent.putExtra("response", response);
                MainActivity2.this.startActivity(myIntent);
            }
        });
    }
}