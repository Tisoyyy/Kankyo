package com.triplea.kankyo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.media.tv.TvContract;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton google;
    TextInputLayout email, password;
    TextInputLayout sEmail, sName, sNumber, sAddress, sPassword;
    Button signButton,loginButton;
    FirebaseFirestore db;
    private CollectionReference noteRef = db.collection("Citizen");

    float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        google = findViewById(R.id.fab_google);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());
                loginButton = findViewById(R.id.l_button);
                signButton = findViewById(R.id.s_button);
                sEmail = findViewById(R.id.s_email);
                sName = findViewById(R.id.name);
                sAddress = findViewById(R.id.address);
                sPassword = findViewById(R.id.spassword);
                //System.out.println(signButton);
                //System.out.println(sEmail);

                signButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        loginUser();

                    } // On Click
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

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTranslationY(300);
        google.setTranslationY(300);

        tabLayout.setAlpha(v);

        google.setAlpha(v);

        tabLayout.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
        google.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();

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
            sPassword.setErrorEnabled(false);
            return true;
        }

    }

    private void loginUser() {

        if(!validateSEmail() | !validateSPassword() | !validateSName() | !validateSAddress()) {
            return;
        }

        String email = sEmail.getEditText().getText().toString().trim();
        String name = sName.getEditText().getText().toString().trim();
        String address = sAddress.getEditText().getText().toString().trim();
        String password = sPassword.getEditText().getText().toString().trim();

        db = FirebaseFirestore.getInstance();
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

        sEmail.getEditText().getText().clear();
        sName.getEditText().getText().clear();
        sAddress.getEditText().getText().clear();
        sPassword.getEditText().getText().clear();

    }

    private void isUser() {

        String userEnteredEmail = sEmail.getEditText().getText().toString().trim();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (DocumentSnapshot.exists()) {

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
