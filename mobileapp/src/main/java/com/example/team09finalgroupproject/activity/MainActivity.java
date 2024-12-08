package com.example.team09finalgroupproject.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.team09finalgroupproject.databinding.ActivityMainBinding;
import com.example.team09finalgroupproject.model.Medication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityMainBinding mainBinding;
    FirebaseFirestore firestore;
    private CollectionReference medicationsRef;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);
        firestore = FirebaseFirestore.getInstance();
        medicationsRef = firestore.collection("medication");

        init();
    }
    private void init(){
        mainBinding.btnSave.setOnClickListener(this);
        mainBinding.btnSelectTime.setOnClickListener(this);
        mainBinding.btnList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String name = mainBinding.edtMedicationName.getText().toString().trim();
        String dosage = mainBinding.edtDosage.getText().toString().trim();
        String time = mainBinding.txtSelectedTime.getText().toString().trim();
        if(v.getId() == mainBinding.btnSave.getId()){
            if(validate()){
                String id = UUID.randomUUID().toString();
                Medication medication = new Medication(id, name, dosage, time);

                medicationsRef.document(id).set(medication).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Medication saved", Toast.LENGTH_SHORT).show();
                            mainBinding.edtMedicationName.setText("");
                            mainBinding.edtDosage.setText("");
                            mainBinding.txtSelectedTime.setText("");


                        } else {
                            Toast.makeText(MainActivity.this, "Failed to save medication", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
        if(v.getId() == mainBinding.btnSelectTime.getId()){
            showTimePicker();
        }
        if(v.getId() == mainBinding.btnList.getId()){
            intent = new Intent(MainActivity.this, MedicationlistActivity.class);
            startActivity(intent);
        }
    }
    private void showTimePicker() {
        // Get current time as default
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                MainActivity.this,
                (view, selectedHour, selectedMinute) -> {
                    // Format the selected time
                    String time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    mainBinding.txtSelectedTime.setText(time);
                },
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }
    private Boolean validate() {

        String name = mainBinding.edtMedicationName.getText().toString().trim();
        String dosage = mainBinding.edtDosage.getText().toString().trim();
        String time = mainBinding.txtSelectedTime.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            mainBinding.edtMedicationName.setError("Medication name is required");
            return false;
        }
        if (TextUtils.isEmpty(dosage)) {
            mainBinding.edtDosage.setError("Dosage is required");
            return false;
        }
        if (TextUtils.isEmpty(time)) {
            mainBinding.txtSelectedTime.setError("Time must be selected");
            return false;
        }
        Calendar currentTime = Calendar.getInstance();
        String[] timeParts = time.split(":");
        int selectedHour = Integer.parseInt(timeParts[0]);
        int selectedMinute = Integer.parseInt(timeParts[1]);

        Calendar selectedTime = Calendar.getInstance();
        selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour);
        selectedTime.set(Calendar.MINUTE, selectedMinute);


        if (selectedTime.before(currentTime)) {
            mainBinding.txtSelectedTime.setError( "Selected time cannot be in the past");
            return false;
        }
        mainBinding.edtMedicationName.setError(null);
        mainBinding.edtDosage.setError(null);
        mainBinding.txtSelectedTime.setError(null);
        return true;

    }

}