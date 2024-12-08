package com.example.team09finalgroupproject.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
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

        init();
    }
    private void init(){
        editBinding.btnSave.setOnClickListener(this);
        editBinding.btnSelectTime.setOnClickListener(this);
        medicationId = intent.getStringExtra("medicationId");
        editBinding.edtName.setText(intent.getStringExtra("name"));
        editBinding.edtDosage.setText(intent.getStringExtra("dosage"));
        editBinding.txtSelectedTime.setText(intent.getStringExtra("time"));
    }

    @Override
    public void onClick(View v) {
        String updatedName = editBinding.edtName.getText().toString().trim();
        String updatedDosage = editBinding.edtDosage.getText().toString().trim();
        String updatedTime = editBinding.txtSelectedTime.getText().toString().trim();

        if(v.getId() == editBinding.btnSave.getId()){

            if (updatedName.isEmpty() || updatedDosage.isEmpty() || updatedTime.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            firestore.collection("medication")
                    .document(medicationId)
                    .update("name", updatedName,
                            "dosage", updatedDosage,
                            "time", updatedTime)
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

}