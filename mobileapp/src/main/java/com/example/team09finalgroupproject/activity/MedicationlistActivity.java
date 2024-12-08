package com.example.team09finalgroupproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.team09finalgroupproject.adapter.MedicationAdapter;
import com.example.team09finalgroupproject.databinding.ActivityMedicationlistBinding;
import com.example.team09finalgroupproject.model.Medication;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

public class MedicationlistActivity extends AppCompatActivity {

    ActivityMedicationlistBinding medicationlistBinding;
    Intent intent;
    private List<Medication> medicationList;
    MedicationAdapter adapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        medicationlistBinding = ActivityMedicationlistBinding.inflate(getLayoutInflater());
        View view = medicationlistBinding.getRoot();
        setContentView(view);

        medicationlistBinding.recyclerViewMedications.setLayoutManager(new LinearLayoutManager(this));
        medicationList = new ArrayList<>();
        adapter = new MedicationAdapter(medicationList);
        medicationlistBinding.recyclerViewMedications.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();

        init();
    }
    private void init(){
        firestore.collection("medication")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    medicationList.clear(); // Clear the existing list
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Medication medication = doc.toObject(Medication.class);
                        if (medication != null) {
                            medication.setId(doc.getId()); // Set the document ID
                            medicationList.add(medication);
                        }
                    }
                    adapter.notifyDataSetChanged(); // Notify the adapter
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch medications", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}