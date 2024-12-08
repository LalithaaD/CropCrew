package com.example.team09finalgroupproject.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team09finalgroupproject.activity.EditActivity;
import com.example.team09finalgroupproject.databinding.MedicationItemListBinding;
import com.example.team09finalgroupproject.model.Medication;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder>{

    List<Medication> medicationList;
    MedicationItemListBinding rowBinding;
    FirebaseFirestore firestore;


    public MedicationAdapter(List<Medication> medicationList) {
        this.medicationList = medicationList;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MedicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        rowBinding = MedicationItemListBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindView(medicationList.get(position).getName(), medicationList.get(position).getDosage(),medicationList.get(position).getTime(),medicationList.get(position).getDate(),medicationList.get(position).getAdherenceStatus());
        Medication medication = medicationList.get(position);
        holder.rowBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("medication")
                        .document(medication.getId()) // Get the unique document ID
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            medicationList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, medicationList.size());
                            Toast.makeText(v.getContext(), "Medication deleted successfully", Toast.LENGTH_SHORT).show();

                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(v.getContext(), "Failed to delete medication", Toast.LENGTH_SHORT).show();

                        });
            }
        });
        holder.rowBinding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditActivity.class);
                intent.putExtra("medicationId", medication.getId());
                intent.putExtra("name", medication.getName());
                intent.putExtra("dosage", medication.getDosage());
                intent.putExtra("time", medication.getTime());
                intent.putExtra("date", medication.getDate());
                intent.putExtra("status", medication.getAdherenceStatus());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        MedicationItemListBinding rowBinding;

        public ViewHolder(MedicationItemListBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding = rowBinding;
        }
        void bindView(final String name,final String dosage,final String time,final String date,final String adherenceStatus){
            rowBinding.txtName.setText(name);
            rowBinding.txtDosage.setText(dosage);
            rowBinding.txtTime.setText(time);
            rowBinding.txtDate.setText(date);
            rowBinding.txtAdherenceStatus.setText(adherenceStatus);
        }

    }
}
