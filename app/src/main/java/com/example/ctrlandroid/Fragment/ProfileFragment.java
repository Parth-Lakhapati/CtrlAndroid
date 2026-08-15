package com.example.ctrlandroid.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ctrlandroid.R;
import com.example.ctrlandroid.URLS.Urls;
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

    EditText etName,etMobileNo,etEmail,etUsername,etAge;
    RadioGroup rgGender;
    int IMAGE_REQUIRED=1;
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        civMyProfile = view.findViewById(R.id.profileImage);
        etName = view.findViewById(R.id.etProfileName);
        etMobileNo = view.findViewById(R.id.etProfileMobileNo);
        etEmail = view.findViewById(R.id.etProfileEmail);
        etUsername = view.findViewById(R.id.etProfileUsername);
        etAge = view.findViewById(R.id.etProfileAge);
        rgGender = view.findViewById(R.id.rgGender);

        int selectedId = rgGender.getCheckedRadioButtonId();

        if (selectedId != -1) {
            RadioButton rbGender = view.findViewById(selectedId);
            String gender = rbGender.getText().toString();

            Toast.makeText(getActivity(), gender, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(),"sorry gender can not ",Toast.LENGTH_SHORT).show();
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();

        civMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,"select image"),IMAGE_REQUIRED);
            }
        });


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("My Profile");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        getMyDetail();
    }

    private void getMyDetail() {
        AsyncHttpClient client= new AsyncHttpClient();
        RequestParams params = new RequestParams();

        String username = preferences.getString("username","");

        if(username.isEmpty()){
            Toast.makeText(getActivity(), "Username Not Found",Toast.LENGTH_LONG).show();
            return;
        }

        params.put("user_name", username);

        client.post(Urls.getMyDetailAPI,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Toast.makeText(getActivity(), "Profile Update", Toast.LENGTH_SHORT).show();


                try {
                    JSONArray jsonArray = response.getJSONArray("getMyDetails");

                    if(jsonArray.length()==0){
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"User Not Found",Toast.LENGTH_LONG).show();
                        return;
                    }

                    for(int i=0;i <jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String strname = jsonObject.getString("name");
                        String strmobileNo = jsonObject.getString("mobile_no");
                        String stremailId = jsonObject.getString("email");
                        String struserName = jsonObject.getString("user_name");
                        String strage = jsonObject.getString("age");

                        String gender = jsonObject.getString("gender");

                        if (gender.equalsIgnoreCase("Male")) {
                            rgGender.check(R.id.rbMale);
                        } else if (gender.equalsIgnoreCase("Female")) {
                            rgGender.check(R.id.rbFemale);
                        } else {
                            rgGender.check(R.id.rbOther);
                        }
                        etName.setText(strname);
                        etMobileNo.setText(strmobileNo);
                        etEmail.setText(stremailId);
                        etUsername.setText(struserName);
                        etAge.setText(strage);

                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Data Error",Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUIRED && resultCode==RESULT_OK && data != null){
            Uri uri=data.getData();

            civMyProfile.setImageURI(uri);
        }
    }
}