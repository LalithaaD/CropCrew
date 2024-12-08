package com.example.team09finalgroupproject.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.team09finalgroupproject.R;
import com.example.team09finalgroupproject.databinding.ActivityEditBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityEditBinding editBinding;
    FirebaseFirestore firestore;
    Intent intent;
    String medicationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editBinding = ActivityEditBinding.inflate(getLayoutInflater());
        View view = editBinding.getRoot();
        setContentView(view);

        firestore = FirebaseFirestore.getInstance();
        intent = getIntent();

        Spinner spinnerAdherenceStatus = editBinding.spinnerAdherenceStatus;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.adherence_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdherenceStatus.setAdapter(adapter);


        init();
    }
    private void init(){
        editBinding.btnSave.setOnClickListener(this);
        editBinding.btnSelectTime.setOnClickListener(this);
        editBinding.btnSelectDate.setOnClickListener(this);
        medicationId = intent.getStringExtra("medicationId");
        editBinding.edtName.setText(intent.getStringExtra("name"));
        editBinding.edtDosage.setText(intent.getStringExtra("dosage"));
        editBinding.txtSelectedTime.setText(intent.getStringExtra("time"));
        editBinding.txtSelectedDate.setText(intent.getStringExtra("date"));
        String adherenceStatus = intent.getStringExtra("adherenceStatus");
        if (adherenceStatus != null) {
            int spinnerPosition = ((ArrayAdapter) editBinding.spinnerAdherenceStatus.getAdapter()).getPosition(adherenceStatus);
            editBinding.spinnerAdherenceStatus.setSelection(spinnerPosition);
        }

    }

    @Override
    public void onClick(View v) {
        String updatedName = editBinding.edtName.getText().toString().trim();
        String updatedDosage = editBinding.edtDosage.getText().toString().trim();
        String updatedTime = editBinding.txtSelectedTime.getText().toString().trim();
        String updatedDate = editBinding.txtSelectedDate.getText().toString().trim();
        String updatedAdherenceStatus = editBinding.spinnerAdherenceStatus.getSelectedItem().toString();


        if(v.getId() == editBinding.btnSave.getId()){

            if (updatedName.isEmpty() || updatedDosage.isEmpty() || updatedTime.isEmpty() || updatedDate.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            firestore.collection("medication")
                    .document(medicationId)
                    .update("name", updatedName,
                            "dosage", updatedDosage,
                            "time", updatedTime,
                            "date", updatedDate,
                            "adherenceStatus", updatedAdherenceStatus)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Medication updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to update medication", Toast.LENGTH_SHORT).show();
                    });
        }
        if(v.getId() == editBinding.btnSelectTime.getId()){
            showTimePicker();
        }
        if (v.getId() == editBinding.btnSelectDate.getId()) {
            showDatePicker();
        }

    }
    private void showTimePicker() {
        // Get current time as default
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                EditActivity.this,
                (view, selectedHour, selectedMinute) -> {
                    // Format the selected time
                    String time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    editBinding.txtSelectedTime.setText(time);
                },
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }
    private void showDatePicker() {
        // Get current date as default
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                EditActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the selected date
                    String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    editBinding.txtSelectedDate.setText(selectedDate);  // Display the selected date
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

}