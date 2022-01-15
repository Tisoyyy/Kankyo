package com.triplea.kankyo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageRegistrar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewReport extends AppCompatActivity {

    TextView vName, vDescription, vLocation, vDate;
    ImageView vPicture;
    Button delete;
    FirebaseFirestore db;
    String email;
    StorageReference reference;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        vName = findViewById(R.id.view_name);
        vDescription = findViewById(R.id.view_description);
        vLocation = findViewById(R.id.view_location);
        vDate = findViewById(R.id.view_date);
        vPicture = findViewById(R.id.view_photo);
        delete = findViewById(R.id.delete_button);
        back = findViewById(R.id.return_button);

        Intent home = new Intent(this, HomePage.class);

        displayReport();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(home);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewReport.this);
                builder.setTitle("Are you sure to delete?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteReport();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });

    }

    private void deleteReport() {
        Intent home = new Intent(ViewReport.this, HomePage.class);
        Intent getData = getIntent();
        String name = getData.getStringExtra("name");

        reference = FirebaseStorage.getInstance().getReference().child("reports/"+email+"/"+name);

        db.collection("Citizen").document(email).collection("Reports").document(name)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    reference.delete();
                    Toast.makeText(ViewReport.this, "Report Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(home);
                    finish();
                }
            }
        });

    }

    private void displayReport() {
        db = FirebaseFirestore.getInstance();
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Intent getData = getIntent();
        String name = getData.getStringExtra("name");

        SimpleDateFormat dateFormatter = new SimpleDateFormat("E, y-M-d");

        db.collection("Citizen").document(email).collection("Reports").document(name).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String displayName = documentSnapshot.getString("reportName");
                            String displayDescription = documentSnapshot.getString("reportDescription");
                            String displayLocation = documentSnapshot.getString("reportLocation");
                            Date displayDate = documentSnapshot.getDate("reportDate");
                            vName.setText(displayName);
                            vDescription.setText(displayDescription);
                            vLocation.setText(displayLocation);
                            vDate.setText(dateFormatter.format(displayDate));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error fetching document", e);
            }
        });

        reference = FirebaseStorage.getInstance().getReference().child("reports/"+email+"/"+name);

        try {
            final File localFile = File.createTempFile(name, "jpg");
            reference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            vPicture.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ViewReport.this, "Picture cannot be retrieved", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}