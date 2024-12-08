package com.example.team09finalgroupproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.team09finalgroupproject.adapter.MedicationAdapter;
import com.example.team09finalgroupproject.databinding.ActivityDashboardBinding;
import com.example.team09finalgroupproject.model.Medication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityDashboardBinding dashboardBinding;
    List<Medication> medicationList;
    MedicationAdapter medicationAdapter;
    Intent intent;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = dashboardBinding.getRoot();
        setContentView(view);

        firestore = FirebaseFirestore.getInstance();
        medicationList = new ArrayList<>();

        dashboardBinding.recyclerViewDashboard.setLayoutManager(new LinearLayoutManager(this));
        medicationAdapter = new MedicationAdapter(medicationList);
        dashboardBinding.recyclerViewDashboard.setAdapter(medicationAdapter);
        loadMedications();
        init();

    }
    private void init(){
        dashboardBinding.btnAddMedication.setOnClickListener(this);
        dashboardBinding.btnMedicationHistory.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == dashboardBinding.btnAddMedication.getId()){
            intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);

        }
        if(v.getId() == dashboardBinding.btnMedicationHistory.getId()){
            intent = new Intent(DashboardActivity.this, HistoryActivity.class);
            startActivity(intent);

        }

    }
    private void loadMedications() {
        firestore.collection("medication").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    medicationList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Medication medication = document.toObject(Medication.class);
                        medicationList.add(medication);
                    }
                    medicationAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DashboardActivity.this, "Failed to load medications", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMedications();
    }
}