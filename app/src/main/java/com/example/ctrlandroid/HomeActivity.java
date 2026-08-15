package com.example.ctrlandroid;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ctrlandroid.Fragment.HomeFragment;
import com.example.ctrlandroid.Fragment.NewFragment;
import com.example.ctrlandroid.Fragment.ProfileFragment;
import com.example.ctrlandroid.Fragment.RegistrationFragment;
import com.example.ctrlandroid.Fragment.SearchFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    FrameLayout btmMenuHomeFrame;
    BottomNavigationView btmMenuHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btmMenuHomeFrame=findViewById(R.id.BottomMenuHomeFrame);
        btmMenuHome=findViewById(R.id.HomeBottomMenuHome);
        btmMenuHome.setOnNavigationItemSelectedListener(this);
        btmMenuHome.setSelectedItemId(R.id.itemHomeBottomHome);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            String name = account.getDisplayName();
            String email = account.getEmail();

            editor.putString("name",name);
            editor.putString("email",email);
            editor.apply();
        }

    }

    HomeFragment homeFragment = new HomeFragment();
    SearchFragment searchFragment = new SearchFragment();
    NewFragment newFragment = new NewFragment();
    RegistrationFragment registrationFragment = new RegistrationFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if(menuItem.getItemId()==R.id.itemHomeBottomHome){
            getSupportFragmentManager().beginTransaction().replace(R.id.BottomMenuHomeFrame,homeFragment).commit();
        }else if(menuItem.getItemId()==R.id.itemHomeBottomSearch){
            getSupportFragmentManager().beginTransaction().replace(R.id.BottomMenuHomeFrame,searchFragment).commit();
        } else if (menuItem.getItemId()==R.id.itemHomeBottomRegistration){
            getSupportFragmentManager().beginTransaction().replace(R.id.BottomMenuHomeFrame,registrationFragment).commit();
        } else if (menuItem.getItemId()==R.id.itemHomeBottomProfile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.BottomMenuHomeFrame,profileFragment).commit();
        }

        return true;
    }
}