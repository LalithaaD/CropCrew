package com.example.team09finalgroupproject.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.team09finalgroupproject.R;
import com.example.team09finalgroupproject.adapter.HistoryAdapter;
import com.example.team09finalgroupproject.databinding.ActivityHistoryBinding;
import com.example.team09finalgroupproject.model.Adherence;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityHistoryBinding historyBinding;
    HistoryAdapter adapter;
    FirebaseFirestore firestore;
    Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyBinding = ActivityHistoryBinding.inflate(getLayoutInflater());
        View view = historyBinding.getRoot();
        setContentView(view);

        firestore = FirebaseFirestore.getInstance();
        historyBinding.rvHistory.setLayoutManager(new LinearLayoutManager(this));

        loadHistoryData(null, null);

        init();
    }
    private void init(){
        historyBinding.btnFilterDate.setOnClickListener(this);
        //historyBinding.btnFilterMedication.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == historyBinding.btnFilterDate.getId()) {
            showDateFilterDialog();
        }
//         else if (v.getId() == historyBinding.btnFilterMedication.getId()) {
//            showMedicationFilterDialog();
//        }
    }
    private void loadHistoryData(String medicationName, String selectedDate) {
        Query query = firestore.collection("medication");

        if (medicationName != null && !medicationName.isEmpty()) {
            query = query.whereEqualTo("medicationName", medicationName);
        }
        if (selectedDate != null && !selectedDate.isEmpty()) {
            query = query.whereEqualTo("date", selectedDate);
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Adherence> historyList = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String id = document.getId(); // Get document ID
                        String medicationId = document.getString("medicationId");
                        String name = document.getString("medicationName");
                        String dosage = document.getString("dosage");
                        String date = document.getString("date");
                        String time = document.getString("time");
                        String adherenceStatus = document.getString("adherenceStatus");

                        historyList.add(new Adherence(id, medicationId, name, dosage, date, time, adherenceStatus));  // Using the full constructor
                    }
                    if (adapter == null) {
                        adapter = new HistoryAdapter(historyList);
                        historyBinding.rvHistory.setAdapter(adapter);
                    } else {
                        adapter.updateList(historyList);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HistoryActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                });
    }

    private void showDateFilterDialog() {
        selectedDate = Calendar.getInstance();

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDate.set(year, month, dayOfMonth);

            // Format the selected date as "yyyy-MM-dd"
            String selectedDateString = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);

            // Load data filtered by the selected date
            loadHistoryData(null, selectedDateString);

        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH)).show();
    }

//    private void showMedicationFilterDialog() {
//        firestore.collection("medication")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    List<String> medicationNames = new ArrayList<>();
//                    for (DocumentSnapshot document : queryDocumentSnapshots) {
//                        String medicationName = document.getString("medicationName");
//                        if (!medicationNames.contains(medicationName)) {
//                            medicationNames.add(medicationName);
//                        }
//                    }
//
//                    if (!medicationNames.isEmpty()) {
//                        // Show medication names in a dropdown (Spinner)
//                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, medicationNames);
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        historyBinding.spinnerMedication.setAdapter(adapter);
//
//                        // Listen for item selection
//                        historyBinding.spinnerMedication.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                                String selectedMedication = medicationNames.get(position);
//                                loadHistoryData(selectedMedication, null);  // Filter by selected medication
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parentView) {
//                                loadHistoryData(null, null);  // Show all data if nothing selected
//                            }
//                        });
//                    } else {
//                        Toast.makeText(this, "No medications found to filter", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load medications", Toast.LENGTH_SHORT).show());
//    }
}
