package com.example.ctrlandroid;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;

public class LoginActivity extends AppCompatActivity {

    boolean double_tap = false;
    EditText etUsername,etPassword;
    CheckBox cbShowPassword;
    TextView tvForgetPassword,tvToRegistration;
    Button btnLogin;
    ProgressDialog progressDialog;
    SharedPreferences preferences ;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        etUsername = findViewById(R.id.etLoginUsername);
//        etPassword = findViewById(R.id.etLoginPassword);
//        cbShowPassword = findViewById(R.id.cbShowPassword);
//        tvForgetPassword = findViewById(R.id.tvLoginForgetPassword);
//        tvToRegistration = findViewById(R.id.tvLoginToRegistration);
//        btnLogin = findViewById(R.id.btnLogin);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etUsername.getText().toString().isEmpty()){
                    etUsername.setError("Please enter Your Username");

                }else if(etPassword.getText().toString().isEmpty()){
                    etPassword.setError("Please enter Your Username");
                }else {

                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Login...");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    loginUser();
                }

            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

                }else{
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.eye_closed,0);
                }
            }
        });

    }

    private void loginUser() {
        progressDialog.dismiss();
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

//    @Override
//    public void onBackPressed() {
//        if(double_tap){
//            finishAffinity();
//        }else{
//            Toast.makeText(LoginActivity.this,"Exit",Toast.LENGTH_SHORT).show();
//            double_tap =true;
//            Handler h=new Handler();
//            h.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    double_tap =false;
//                }
//            },2000);
//        }
//    }
}