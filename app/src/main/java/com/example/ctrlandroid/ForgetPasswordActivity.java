package com.example.ctrlandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ctrlandroid.URLS.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText etUsername,etPassword,etConfirmPassword;
    Button btnForgetPassword;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        etUsername = findViewById(R.id.etForgotUsername);
        etPassword = findViewById(R.id.etForgotNewPassword);
        etConfirmPassword = findViewById(R.id.etForgotConfirmPassword);

        btnForgetPassword = findViewById(R.id.btnForgotContinue);

        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etUsername.getText().toString().isEmpty()){
                    etUsername.setError("Please enter UserName");
                }else if(etPassword.getText().toString().isEmpty()){
                    etPassword.setError("Please enter Password");
                } else if (etPassword.getText().toString().length() < 8) {
                    etPassword.setError("Password must be greater than 8");
                }else if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString())){
                    etConfirmPassword.setError("Password and Confirm Password Is not match");
                }else{

                    progressDialog = new ProgressDialog(ForgetPasswordActivity.this);
                    progressDialog.setTitle("Forget Password");
                    progressDialog.setMessage("Please wait");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    forgetPassword();
                }
            }
        });

    }

    private void forgetPassword() {

        AsyncHttpClient client= new AsyncHttpClient();
        RequestParams params =new RequestParams();

        params.put("user_name",etUsername.getText().toString());
        params.put("password",etPassword.getText().toString());

        client.post(Urls.forgetPasswordAPI,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();

                try {
                    String status =response.getString("success");
                    String msg = response.getString("message");

                    if(status.equals("1")){
                        Toast.makeText(ForgetPasswordActivity.this,msg,Toast.LENGTH_SHORT).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(ForgetPasswordActivity.this,LoginActivity.class);
                                startActivity(i);
                                finishAffinity();
                            }
                        },3000);
                    }else{
                        Toast.makeText(ForgetPasswordActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(ForgetPasswordActivity.this,"Can not reached this page",Toast.LENGTH_SHORT).show();
            }
        });
    }
}