package com.triplea.kankyo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class CreateReport extends AppCompatActivity {

    ImageView create_return;
    String email;
    TextInputLayout rName, rDescription, rLocation, rDate;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        create_return = findViewById(R.id.create_return);
        rName = findViewById(R.id.r_name);
        rDescription = findViewById(R.id.r_description);
        rLocation = findViewById(R.id.r_location);
        rDate = findViewById(R.id.r_date);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        Intent passEmail = getIntent();
        Intent intent = new Intent(this, HomePage.class);

        email = passEmail.getStringExtra("email");

        create_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }
        });

    }
}