package com.triplea.kankyo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;

public class HomePage extends AppCompatActivity {

    FloatingActionButton reportBttn;
    TextView dispName, dispEmail;
    String email;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent intent = getIntent();

        reportBttn = findViewById(R.id.report_button);
        dispName = findViewById(R.id.display_name);
        dispEmail = findViewById(R.id.display_email);

        email = intent.getStringExtra("email");
        System.out.println(email);
        displayUser();

    }

    private void displayUser() {
        db = FirebaseFirestore.getInstance();

        db.collection("Citizen").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String displayEmail = documentSnapshot.getId();
                            String displayName = documentSnapshot.getString("Name");
                            dispName.setText(displayName);
                            dispEmail.setText(displayEmail);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}