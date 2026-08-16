package com.example.ctrlandroid.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ctrlandroid.LoginActivity;
import com.example.ctrlandroid.R;
import com.example.ctrlandroid.comman.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView civMyProfile;

    EditText etName, etMobileNo, etEmail, etUsername, etAge;

    RadioGroup rgGender;

    Button btnLogout;

    int IMAGE_REQUIRED = 1;

    ProgressDialog progressDialog;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(    R.layout.fragment_profile,    container,
                false
        );

        civMyProfile = view.findViewById(R.id.profileImage);
        etName = view.findViewById(R.id.etProfileName);
        etMobileNo = view.findViewById(R.id.etProfileMobileNo);
        etEmail = view.findViewById(R.id.etProfileEmail);
        etUsername = view.findViewById(R.id.etProfileUsername);
        etAge = view.findViewById(R.id.etProfileAge);
        rgGender = view.findViewById(R.id.rgGender);
        btnLogout = view.findViewById(R.id.btnLogout);


        preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        editor = preferences.edit();


        civMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(  Intent.createChooser( intent,"Select Image"  ),  IMAGE_REQUIRED
                );
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog =      new AlertDialog.Builder(requireActivity());
                alertDialog.setTitle("Logout the Account");
                alertDialog.setMessage(  "Do you really want to Logout your account" );
                alertDialog.setPositiveButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );

                alertDialog.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(  DialogInterface dialog,  int which) {
                                editor.putBoolean( "isLogin", false);
                                editor.remove("username");
                                editor.apply();
                                Intent intent = new Intent(requireActivity(),LoginActivity.class);
                                startActivity(intent);
                                requireActivity().finish();
                            }
                        }
                );
                alertDialog.show();
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog( requireActivity());
        progressDialog.setTitle("My Profile");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        getMyDetail();
    }

    private void getMyDetail() {
        AsyncHttpClient client =new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String username =  preferences.getString( "username","");

        if (username.isEmpty()) {
            if (progressDialog != null &&   progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            Toast.makeText(  requireActivity(),  "Username Not Found",  Toast.LENGTH_LONG
            ).show();
            return;
        }
        params.put(   "username",  username
        );


        client.post(  Urls.getMyDetailAPI,  params,    new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess( int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(  statusCode,  headers,  response);


                        try {

                            JSONArray jsonArray = response.getJSONArray(   "getMyDetails");

                            if (jsonArray.length() == 0) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Toast.makeText( requireActivity(), "User Not Found", Toast.LENGTH_LONG).show();
                                return;
                            }

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String name = jsonObject.getString("name");
                            String mobileNo = jsonObject.getString( "mobileno");
                            String emailId =jsonObject.getString("emailid");
                            String userName = jsonObject.getString("username");
                            String age =jsonObject.getString("age");
                            String gender =jsonObject.getString("gender");


                            etName.setText(name);
                            etMobileNo.setText(mobileNo);
                            etEmail.setText(emailId);
                            etUsername.setText(userName);
                            etAge.setText(age);


                            if (gender.equalsIgnoreCase("Male")) {
                                rgGender.check(   R.id.rbMale);

                            } else if (gender.equalsIgnoreCase("Female")) {
                                rgGender.check(R.id.rbFemale);

                            } else {
                                rgGender.check(R.id.rbOther
                                );
                            }

                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {

                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(requireActivity(), "Data Error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure( int statusCode,Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        Toast.makeText(requireActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                        throwable.printStackTrace();
                    }
                }
        );
    }

    @Override
    public void onActivityResult(   int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUIRED && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                civMyProfile.setImageURI(uri);
            }
        }
    }
}