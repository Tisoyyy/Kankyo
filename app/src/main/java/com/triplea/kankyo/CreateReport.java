package com.triplea.kankyo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Calendar;

public class CreateReport extends AppCompatActivity {

    ImageView create_return;
    String email, userName;
    TextInputLayout rName, rDescription, rBarangay, rDate, rCity;
    Button nextPage;
    Intent next;

    //TODO: Set current time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        create_return = findViewById(R.id.create_return);
        rName = findViewById(R.id.r_name);
        rDescription = findViewById(R.id.r_description);
        rBarangay = findViewById(R.id.r_barangay);
        rDate = findViewById(R.id.r_date);
        nextPage = findViewById(R.id.next_page);
        rCity = findViewById(R.id.r_city);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());

        rDate.getEditText().setText(currentDate);

        Intent passEmail = getIntent();
        next = new Intent(this, UploadPhoto.class);
        Intent intent = new Intent(this, HomePage.class);

        email = passEmail.getStringExtra("email");
        userName = passEmail.getStringExtra("name");

        rDate.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateReport.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day+"/"+month+"/"+year;
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

        rDate.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                rDate.setError(null);
            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateName() | !validateDescription() | !validateBarangay() | !validateDate() | !validateCity()) {
                    return;
                }

                String name = rName.getEditText().getText().toString().trim();
                String description = rDescription.getEditText().getText().toString().trim();
                String barangay = rBarangay.getEditText().getText().toString().trim();
                String city = rCity.getEditText().getText().toString().trim();
                String date = rDate.getEditText().getText().toString().trim();

                String reportLocation = barangay + ", " + city;

                next.putExtra("name", name);
                next.putExtra("description", description);
                next.putExtra("location", reportLocation);
                next.putExtra("date", date);
                next.putExtra("email", email);
                next.putExtra("reportName", userName);
                startActivity(next);
                finish();
            }
        });

    }

    private Boolean validateName() {
        String val = rName.getEditText().getText().toString();

        if (val.isEmpty()) {
            rName.setError("Field cannot be Empty");
            return false;
        } else {
            rName.setError(null);
            rName.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateDescription() {
        String val = rDescription.getEditText().getText().toString();

        if (val.isEmpty()) {
            rDescription.setError("Field cannot be Empty");
            return false;
        } else {
            rDescription.setError(null);
            rDescription.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateBarangay() {
        String val = rBarangay.getEditText().getText().toString();

        if (val.isEmpty()) {
            rBarangay.setError("Field cannot be Empty");
            return false;
        } else {
            rBarangay.setError(null);
            rBarangay.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateCity() {
        String val = rCity.getEditText().getText().toString();

        if (val.isEmpty()) {
            rCity.setError("Field cannot be Empty");
            return false;
        } else {
            rCity.setError(null);
            rCity.setErrorEnabled(false);
            return true;
        }

    }
    private Boolean validateDate() {
        String val = rDate.getEditText().getText().toString();

        if (val.isEmpty()) {
            rDate.setError("Field cannot be Empty");
            return false;
        } else {
            rDate.setError(null);
            rDate.setErrorEnabled(false);
            return true;
        }

    }


}