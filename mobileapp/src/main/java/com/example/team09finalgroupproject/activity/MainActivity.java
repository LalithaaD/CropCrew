package com.example.team09finalgroupproject.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.team09finalgroupproject.R;
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

        Spinner spinnerAdherenceStatus = mainBinding.spinnerAdherenceStatus;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.adherence_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdherenceStatus.setAdapter(adapter);

        init();
    }
    private void init(){
        mainBinding.btnSave.setOnClickListener(this);
        mainBinding.btnSelectTime.setOnClickListener(this);
        mainBinding.btnSelectDate.setOnClickListener(this);
        mainBinding.btnList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String name = mainBinding.edtMedicationName.getText().toString().trim();
        String dosage = mainBinding.edtDosage.getText().toString().trim();
        String time = mainBinding.txtSelectedTime.getText().toString().trim();
        String selectedAdherenceStatus = mainBinding.spinnerAdherenceStatus.getSelectedItem().toString();
        String date = mainBinding.txtSelectedDate.getText().toString().trim();

        if(v.getId() == mainBinding.btnSave.getId()){
            if(validate()){
                String id = UUID.randomUUID().toString();

                Medication medication = new Medication(id, name, dosage, time,date,selectedAdherenceStatus);

                medicationsRef.document(id).set(medication).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Medication saved", Toast.LENGTH_SHORT).show();
                            mainBinding.edtMedicationName.setText("");
                            mainBinding.edtDosage.setText("");
                            mainBinding.txtSelectedTime.setText("");
                            mainBinding.spinnerAdherenceStatus.setSelection(0);
                            mainBinding.txtSelectedDate.setText("");


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
        if (v.getId() == mainBinding.btnSelectDate.getId()) {
            showDatePicker();
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
    private void showDatePicker() {
        // Get current date as default
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the selected date
                    String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    mainBinding.txtSelectedDate.setText(selectedDate);  // Display the selected date
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

    private Boolean validate() {

        String name = mainBinding.edtMedicationName.getText().toString().trim();
        String dosage = mainBinding.edtDosage.getText().toString().trim();
        String time = mainBinding.txtSelectedTime.getText().toString().trim();
        String date = mainBinding.txtSelectedDate.getText().toString().trim();

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
        currentTime.set(Calendar.HOUR_OF_DAY, 0); // Reset to midnight
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.SECOND, 0);
        currentTime.set(Calendar.MILLISECOND, 0);

        String[] timeParts = time.split(":");
        int selectedHour = Integer.parseInt(timeParts[0]);
        int selectedMinute = Integer.parseInt(timeParts[1]);

        Calendar selectedTime = Calendar.getInstance();
        selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour);
        selectedTime.set(Calendar.MINUTE, selectedMinute);
        selectedTime.set(Calendar.SECOND, 0);
        selectedTime.set(Calendar.MILLISECOND, 0);

        if (selectedTime.before(currentTime)) {
            mainBinding.txtSelectedTime.setError("Selected time cannot be in the past");
            return false;
        }

        if (TextUtils.isEmpty(date)) {
            mainBinding.txtSelectedDate.setError("Date must be selected");
            return false;
        }

        String[] dateParts = date.split("-");
        int selectedYear = Integer.parseInt(dateParts[0]);
        int selectedMonth = Integer.parseInt(dateParts[1]) - 1; // Month is 0-based
        int selectedDay = Integer.parseInt(dateParts[2]);

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(selectedYear, selectedMonth, selectedDay);
        selectedDate.set(Calendar.HOUR_OF_DAY, 0); // Reset to midnight for proper comparison
        selectedDate.set(Calendar.MINUTE, 0);
        selectedDate.set(Calendar.SECOND, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);

        if (selectedDate.before(currentTime)) {
            mainBinding.txtSelectedDate.setError("Selected date cannot be in the past");
            return false;
        }

        if (mainBinding.spinnerAdherenceStatus.getSelectedItemPosition() == 0) {
            Toast.makeText(MainActivity.this, "Please select adherence status", Toast.LENGTH_SHORT).show();
            return false;
        }

        mainBinding.edtMedicationName.setError(null);
        mainBinding.edtDosage.setError(null);
        mainBinding.txtSelectedTime.setError(null);
        mainBinding.txtSelectedDate.setError(null);
        return true;

    }
}
