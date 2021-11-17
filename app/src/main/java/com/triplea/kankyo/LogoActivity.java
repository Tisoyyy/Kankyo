package com.triplea.kankyo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogoActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_activity);

        auth = FirebaseAuth.getInstance();

        Intent startHome = new Intent(this, HomePage.class);
        Intent startLogin = new Intent(this, LoginActivity.class);

        //if (auth.sign)

        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser!=null) {
                    startActivity(startHome);
                    finish();
                }
                else {
                    startActivity(startLogin);
                    finish();
                }
            }

        }.start();

    }

}