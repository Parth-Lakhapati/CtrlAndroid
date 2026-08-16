package com.example.ctrlandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WelcomeActivity extends AppCompatActivity {

    Button btnWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        btnWelcome= findViewById(R.id.btnWelcome);

        //Logic
        btnWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(
                        WelcomeActivity.this,
                        "Started Successfully",
                        Toast.LENGTH_SHORT
                ).show();
                Intent i = new Intent(WelcomeActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });



    }
}