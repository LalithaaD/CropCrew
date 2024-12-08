package com.example.team09finalgroupproject.activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.team09finalgroupproject.R;
import com.example.team09finalgroupproject.adapter.HistoryAdapter;
import com.example.team09finalgroupproject.databinding.ActivityHistoryBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityHistoryBinding historyBinding;
    HistoryAdapter adapter;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyBinding = ActivityHistoryBinding.inflate(getLayoutInflater());
        View view = historyBinding.getRoot();
        setContentView(view);

        firestore = FirebaseFirestore.getInstance();
        historyBinding.rvHistory.setLayoutManager(new LinearLayoutManager(this));
        init();
    }
    private void init(){
        historyBinding.btnFilterDate.setOnClickListener(this);
        historyBinding.btnFilterMedication.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}