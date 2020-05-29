package com.example.poskorafinaru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private SharedPrefManager sharedPrefManager;
    boolean doubleBackToExitPressedOnce = false;
    private Button buttonLogin;
    private EditText editTextUserName;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextUserName= (EditText) findViewById(R.id.editTextUsername);

        sharedPrefManager = SharedPrefManager.getInstance(this);
        if(sharedPrefManager.isLoggedIn()){
            Intent bukaMainActivity = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(bukaMainActivity);
        }

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextUserName.getText().toString().toLowerCase().contains("roc") && editTextPassword.getText().toString().toLowerCase().contains("rochebat")){
                    sharedPrefManager.login();
                    Intent bukaMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                    startActivityForResult(bukaMainActivity,1);
                } else if (editTextUserName.getText().toString().toLowerCase().contains("ta") && editTextPassword.getText().toString().toLowerCase().contains("tahebat")) {
                    sharedPrefManager.login();
                    Intent bukaMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                    startActivityForResult(bukaMainActivity,1);
                } else Toast.makeText(LoginActivity.this, "Invalid username and password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}