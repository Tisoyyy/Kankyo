package com.triplea.kankyo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TextInputLayout lEmail, lPassword;
    TextInputLayout sEmail, sName, sAddress, sPassword;
    Button signButton,loginButton;
    FirebaseFirestore db;
    FirebaseAuth auth;
    ProgressBar progressBar, sProgressBar;

    float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTranslationY(300);

        tabLayout.setAlpha(v);

        tabLayout.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());
                loginButton = findViewById(R.id.l_button);
                signButton = findViewById(R.id.s_button);
                lEmail = findViewById(R.id.lemail);
                lPassword = findViewById(R.id.lpassword);
                sEmail = findViewById(R.id.s_email);
                sName = findViewById(R.id.name);
                sAddress = findViewById(R.id.address);
                sPassword = findViewById(R.id.spassword);
                progressBar = findViewById(R.id.prog_bar);
                sProgressBar = findViewById(R.id.signup_progbar);

                signButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sProgressBar.setVisibility(View.VISIBLE);
                        signupUser();

                        sPassword.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean b) {
                                sPassword.setError(null);
                            }
                        });

                        sAddress.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean b) {
                                sAddress.setError(null);
                            }
                        });

                    } // On Click
                });

                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println(lEmail);
                        progressBar.setVisibility(View.VISIBLE);
                        loginUser();

                        lPassword.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean b) {
                                lPassword.setError(null);
                            }
                        });
                    }
                });

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });

    }

    private Boolean validateLEmail() {
        String val = lEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            lEmail.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            lEmail.setError("Invalid Email Address");
            return false;
        } else {
            lEmail.setError(null);
            lEmail.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateLPassword() {
        String val = lPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            lPassword.setError("Field cannot be Empty");
            return false;
        } else {
            lPassword.setError(null);
            return true;
        }

    }

    private Boolean validateSEmail() {
        String val = sEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            sEmail.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            sEmail.setError("Invalid Email Address");
            return false;
        } else {
            sEmail.setError(null);
            sEmail.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateSName() {
        String val = sName.getEditText().getText().toString();

        if (val.isEmpty()) {
            sName.setError("Field cannot be Empty");
            return false;
        } else {
            sName.setError(null);
            sName.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateSAddress() {
        String val = sAddress.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            sAddress.setError("Field cannot be Empty");
            return false;
        } else if (val.matches(noWhiteSpace)) {
            sAddress.setError("Field cannot contain White Space");
            return false;
        } else {
            sAddress.setError(null);
            sAddress.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateSPassword() {
        String val = sPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            sPassword.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            sPassword.setError("Password is too weak");
            return false;
        } else {
            sPassword.setError(null);
            return true;
        }

    }

    private void signupUser() {

        String email = sEmail.getEditText().getText().toString().trim();
        String name = sName.getEditText().getText().toString().trim();
        String address = sAddress.getEditText().getText().toString().trim();
        String password = sPassword.getEditText().getText().toString().trim();

        if(!validateSEmail() | !validateSPassword() | !validateSName() | !validateSAddress()) {
            sProgressBar.setVisibility(View.INVISIBLE);
            return;
        }

        Intent intent = new Intent(this, HomePage.class);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("https://www.example.com/finishSignUp?cartId=1234")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setIOSBundleId("com.example.ios")
                        .setAndroidPackageName(
                                "com.example.android",
                                true, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_LONG).show();
                    auth.sendSignInLinkToEmail(email, actionCodeSettings)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                    }
                                }
                            });
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    sProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        Map<String, Object> user = new HashMap<>();
        user.put("Email", email);
        user.put("Name", name);
        user.put("Address", address);
        user.put("password", password);

        db.collection("Citizen").document(email).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId())
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    private void loginUser() {

        if (!validateLEmail() | !validateLPassword()) {
            return;
        }

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Intent intent = new Intent(this, HomePage.class);

        String userEnteredEmail = lEmail.getEditText().getText().toString().trim();
        String userEnteredPassword = lPassword.getEditText().getText().toString().trim();

        intent.putExtra("email", userEnteredEmail);

        auth.signInWithEmailAndPassword(userEnteredEmail, userEnteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                    intent.putExtra("email", userEnteredEmail);
                    startActivity(intent);
                    finish();
                } else Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
