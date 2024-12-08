package com.example.team09finalgroupproject.activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.team09finalgroupproject.R;
import com.example.team09finalgroupproject.databinding.ActivityHistoryBinding;

public class HistoryActivity extends AppCompatActivity {

    ActivityHistoryBinding historyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyBinding = ActivityHistoryBinding.inflate(getLayoutInflater());
        View view = historyBinding.getRoot();
        setContentView(view);
        init();
    }
    private void init(){

    }
}