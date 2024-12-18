package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private Button button;

    private static final String TEXT_WELCOME = "Welcome to my Vacation Planner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();
        setInitialValues();

        // Set OnClickListener for the button using lambda
        button.setOnClickListener(v -> {
            // Handle button click
            Intent intent = new Intent(MainActivity.this, VacationList.class);
            startActivity(intent);
        });
    }

    private void initializeUI() {
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
    }

    private void setInitialValues() {
        textView.setText(TEXT_WELCOME);
        imageView.setImageResource(R.drawable.baseline_home_24);
    }
}
