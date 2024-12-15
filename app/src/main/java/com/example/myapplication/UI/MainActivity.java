package com.example.myapplication.UI;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    // Declares UI elements from res>layout>activity_main.xml
    private TextView textView;
    private ImageView imageView;
    private Button button;

    // Constant for welcome message
    private static final String TEXT_WELCOME = "Welcome to my Vacation Planner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements by finding them by their IDs
        initializeUI();

        // Set initial values for TextView and ImageView
        setInitialValues();
    }

    // Method to initialize UI elements
    private void initializeUI() {
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
    }

    // Method to set initial values for TextView and ImageView
    private void setInitialValues() {
        textView.setText(TEXT_WELCOME);
        imageView.setImageResource(R.drawable.baseline_home_24);
    }
}
