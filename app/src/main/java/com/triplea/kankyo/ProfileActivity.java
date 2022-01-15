package com.triplea.kankyo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    TextInputLayout name, email, address;
    Button changePassword, logout;
    FirebaseFirestore db;
    String uEmail;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent home = new Intent(this, HomePage.class);
        Intent startLogin = new Intent(this, LoginActivity.class);
        ProgressDialog pd = new ProgressDialog(this);

        name = findViewById(R.id.p_name);
        email = findViewById(R.id.p_email);
        address = findViewById(R.id.p_address);
        changePassword = findViewById(R.id.change_pass);
        logout = findViewById(R.id.logout_bttn);
        back = findViewById(R.id.return_bttn2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(home);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(startLogin);
                finish();
            }
        });

        displayUser();

    }

    private void displayUser() {
        db = FirebaseFirestore.getInstance();
        uEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        db.collection("Citizen").document(uEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String displayName = documentSnapshot.getString("Name");
                    String displayEmail = documentSnapshot.getString("Email");
                    String displayAddress = documentSnapshot.getString("Address");
                    name.getEditText().setText(displayName);
                    email.getEditText().setText(displayEmail);
                    address.getEditText().setText(displayAddress);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error loading data", e);
            }
        });

    }

}