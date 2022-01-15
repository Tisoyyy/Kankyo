package com.triplea.kankyo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UploadPhoto extends AppCompatActivity {

    ImageView returnButton, uploadPhoto;
    String email, name, senderName;
    Button createReport;

    FirebaseAuth firebaseAuth;
    FirebaseStorage storage;
    FirebaseFirestore db;
    CollectionReference reference;
    ProgressDialog pd;

    Uri imageUri;
    StorageReference storageReference;
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    private Bitmap BitmapDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        storage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = storage.getReference();

        createReport = findViewById(R.id.create_report);
        returnButton = findViewById(R.id.return_bttn);
        uploadPhoto = (ImageView) findViewById(R.id.upload_photo);

        Intent getValues = getIntent();
        Intent returnPage = new Intent(this, CreateReport.class);
        Intent home = new Intent(this, HomePage.class);

        email = getValues.getStringExtra("email");
        senderName = getValues.getStringExtra("reportName");

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnPage.putExtra("email", email);
                startActivity(returnPage);
                finish();
            }
        });

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
                home.putExtra("email", email);
            }
        });

        createReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadPhoto();
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void sendEmail(Bitmap bitmap) throws MessagingException {
        Intent getValues = getIntent();

        String name = getValues.getStringExtra("name");
        String description = getValues.getStringExtra("description");
        String location = getValues.getStringExtra("location");
        String date = getValues.getStringExtra("date");
        String username = "kankyoenvironmentalapp@gmail.com";
        String password = "*kankyoapp2021";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(username));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("josiahalga@gmail.com"));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            message.setSubject(name);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Multipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = String.format("<p>Report Name: %s<br>Report Description: %s<br>Report Location: %s<br>Report Date: %s" +
                        "<br>Report Sender: %s<br>Email: %s</p>", name, description, location, date, senderName, email);
        messageBodyPart.setContent(htmlText, "text/html");
        multipart.addBodyPart(messageBodyPart);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();
        MimeBodyPart imageBodyPart = new MimeBodyPart();
        ByteArrayDataSource bds = new ByteArrayDataSource(imageInByte, "image/jpeg");
        imageBodyPart.setDataHandler(new DataHandler(bds));
        imageBodyPart.setHeader("Content-ID", "<image>");
        imageBodyPart.setFileName(name + ".jpg");
        multipart.addBodyPart(imageBodyPart);

        try {
            message.setText("Report Name: " + name + "\nReport Description: " + description + "\nReport Location: " + location
                    + "\nReport Date: " + date);
            message.setContent(multipart);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "Email Sent Successfully", Toast.LENGTH_LONG).show();

    }

    private void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 100 && data != null && data.getData() != null) {

                imageUri = data.getData();
                uploadPhoto.setImageURI(imageUri);
            }

    }

    private void uploadPhoto() {

        final ProgressDialog pd = new ProgressDialog(this);
        Intent getValues = getIntent();
        Intent home = new Intent(this, HomePage.class);

        pd.setMessage("Uploading Photo");
        pd.show();

        name = getValues.getStringExtra("name");
        StorageReference mountainsRef = storageReference.child("reports/"+email+"/"+name);

        if (imageUri == null) {
            pd.dismiss();
            Toast.makeText(this, "Please Select a Photo", Toast.LENGTH_SHORT).show();
        }
        else {
            createReport();
            mountainsRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                    pd.dismiss();
                    startActivity(home);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void createReport() {

        Intent getValues = getIntent();

        String email = getValues.getStringExtra("email");
        String name = getValues.getStringExtra("name");
        String description = getValues.getStringExtra("description");
        String location = getValues.getStringExtra("location");
        String date = getValues.getStringExtra("date");

        Bitmap bitmap = ((BitmapDrawable) uploadPhoto.getDrawable()).getBitmap();

        db = FirebaseFirestore.getInstance();
        reference = db.collection("Citizen").document(email).collection("Reports");

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

        Map<String, Object> user = new HashMap<>();
        user.put("reportName", name);
        user.put("reportDescription", description);
        user.put("reportLocation", location);
        user.put("reportDate", getDateFromString(date));

        reference.document(name).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        try {
                            sendEmail(bitmap);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
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

    public Date getDateFromString(String datetoSaved){

        try {
            Date date = format.parse(datetoSaved);
            return date;
        } catch (ParseException e){
            return null ;
        }

    }

}