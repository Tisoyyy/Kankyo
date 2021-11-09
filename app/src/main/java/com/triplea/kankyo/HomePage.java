package com.triplea.kankyo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    FloatingActionButton reportBttn;
    TextView dispName, dispEmail;
    String email;
    RecyclerView recyclerView;
    ArrayList<User> userArrayList;
    RVAdapter rvAdapter;
    ProgressDialog progressDialog;
    FloatingActionButton report;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data");
        progressDialog.show();

        Intent intent = getIntent();

        recyclerView = findViewById(R.id.recycler_view);
        reportBttn = findViewById(R.id.report_button);
        dispName = findViewById(R.id.display_name);
        dispEmail = findViewById(R.id.display_email);
        report = findViewById(R.id.report_button);

        email = intent.getStringExtra("email");
        System.out.println(email);
        displayUser();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<>();

        rvAdapter = new RVAdapter(HomePage.this, userArrayList);
        recyclerView.setAdapter(rvAdapter);

        EventChangeListener();
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(userArrayList.size());
            }
        });

    }

    private void EventChangeListener() {

        db.collection("Citizen")
                .document("josiahalga@gmail.com")
                .collection("Reports").orderBy("reportDate", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            if (progressDialog.isShowing()) progressDialog.dismiss();
                            Log.e("Database Error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                userArrayList.add(dc.getDocument().toObject(User.class));
                            }

                            rvAdapter.notifyDataSetChanged();
                            if (progressDialog.isShowing()) progressDialog.dismiss();

                        }


                    }
                });

    }

    private void displayUser() {
        db = FirebaseFirestore.getInstance();

        db.collection("Citizen").document("algazephaniah@gmail.com").get()
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
                Log.w(TAG, "Error fetching document", e);
            }
        });
    }

}