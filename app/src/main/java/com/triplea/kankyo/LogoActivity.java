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

        //if (auth.sign)

        Intent intent = new Intent(this, LoginActivity.class);
        Intent intent2 = new Intent(this, HomePage.class);
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                /*FirebaseUser fUser = auth.getCurrentUser();
                if (fUser != null) {
                    startActivity(intent2);
                } else startActivity(intent);*/
                startActivity(intent);
            }

        }.start();

    }



}