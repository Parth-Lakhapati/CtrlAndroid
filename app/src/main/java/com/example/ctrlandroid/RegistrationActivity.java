package com.example.ctrlandroid;

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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ctrlandroid.URLS.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends AppCompatActivity {

    boolean double_tap=false;
    EditText etName,etMobileNo,etEmail,etUsername,etPassword;
    CheckBox cbShowPassword;
    Button btnRegistration;
    AppCompatButton acbtnGoogle;
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

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
        acbtnGoogle = findViewById(R.id.acbtnGoogleRegistration);

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

        acbtnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,1234);
    }
    private void registrationUser() {
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();

        params.put("name",etName.getText().toString().trim());
        params.put("mobile_no",etMobileNo.getText().toString().trim());
        params.put("email",etEmail.getText().toString().trim());
        params.put("user_name",etUsername.getText().toString().trim());
        params.put("password",etPassword.getText().toString().trim());

        android.util.Log.d("API_URL", Urls.registerUserAPI);

        client.post(Urls.registerUserAPI,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){

                progressDialog.dismiss();

                try{

                    String success=response.getString("success");
                    String message=response.getString("message");

                    if(success.equals("1")){
                        Toast.makeText(RegistrationActivity.this,message,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                        finishAffinity();
                    }
                    else{
                        Toast.makeText(RegistrationActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch(JSONException e){
                    Toast.makeText(RegistrationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                String error = "Status Code: " + statusCode;
                if (throwable != null) {
                    error += "\n" + throwable.toString();
                }
                Toast.makeText(RegistrationActivity.this, error, Toast.LENGTH_LONG).show();
                throwable.printStackTrace();
            }
        });
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1234){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToHomeActivity();
            } catch (ApiException e) {
                Toast.makeText(RegistrationActivity.this, ""+e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(RegistrationActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}