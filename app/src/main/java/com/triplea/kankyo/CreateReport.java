package com.triplea.kankyo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class CreateReport extends AppCompatActivity {

    ImageView create_return;
    String email;
    TextInputLayout rName, rDescription, rLocation, rDate;
    Button nextPage;
    Intent next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        create_return = findViewById(R.id.create_return);
        rName = findViewById(R.id.r_name);
        rDescription = findViewById(R.id.r_description);
        rLocation = findViewById(R.id.r_location);
        rDate = findViewById(R.id.r_date);
        nextPage = findViewById(R.id.next_page);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        Intent passEmail = getIntent();
        next = new Intent(this, UploadPhoto.class);
        Intent intent = new Intent(this, HomePage.class);

        email = passEmail.getStringExtra("email");

        rDate.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateReport.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = year+"/"+month+"/"+day;
                        rDate.getEditText().setText(date);
                    }
                }, year,month,day);
                datePickerDialog.show();
            }
        });

        create_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = rName.getEditText().getText().toString().trim();
                String description = rDescription.getEditText().getText().toString().trim();
                String location = rLocation.getEditText().getText().toString().trim();
                String date = rDate.getEditText().getText().toString().trim();

                next.putExtra("name", name);
                next.putExtra("description", description);
                next.putExtra("location", location);
                next.putExtra("date", date);
                next.putExtra("email", email);
                System.out.println(email);
                startActivity(next);
                finish();
            }
        });

    }


}