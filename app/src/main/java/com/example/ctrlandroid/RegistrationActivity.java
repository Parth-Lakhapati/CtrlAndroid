package com.example.ctrlandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistrationActivity extends AppCompatActivity {

    boolean double_tap=false;
    EditText etName,etMobileNo,etEmail,etUsername,etPassword;
    CheckBox cbShowPassword;
    Button btnRegistration;
    AppCompatButton acbtnGoogle;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etName = findViewById(R.id.etRegistrationName);
        etMobileNo = findViewById(R.id.etRegistrationMobile);
        etEmail = findViewById(R.id.etRegistrationEmail);
        etUsername = findViewById(R.id.etRegistrationUsername);
        etPassword = findViewById(R.id.etRegistrationPassword);
        cbShowPassword = findViewById(R.id.cbRegistrationShowPassword);
        btnRegistration = findViewById(R.id.btnRegistration);
        acbtnGoogle = findViewById(R.id.btnGoogleRegistration);

        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().toString().isEmpty()){
                    etName.setError("Please enter Your Name");
                }else if(etMobileNo.getText().toString().isEmpty()){
                    etMobileNo.setError("Please enter Your Mobile No");
                }else if(etEmail.getText().toString().isEmpty()){
                    etEmail.setError("Please enter Your Email");
                }else if(etUsername.getText().toString().isEmpty()){
                    etUsername.setError("Please enter Your Username");
                }else if(etPassword.getText().toString().isEmpty()){
                    etPassword.setError("Please enter Your Password");
                }else{
                    progressDialog = new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setTitle("Login...");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();
                    registrationUser();
                }
            }
        });

        acbtnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void registrationUser() {
        progressDialog.dismiss();
        Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(double_tap){
            finishAffinity();
        }else{
            Toast.makeText(RegistrationActivity.this,"Exit",Toast.LENGTH_SHORT).show();
            double_tap =true;
            Handler h=new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    double_tap =false;
                }
            },2000);
        }
    }
}