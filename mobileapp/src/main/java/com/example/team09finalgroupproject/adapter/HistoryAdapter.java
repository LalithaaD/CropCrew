package com.example.team09finalgroupproject.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team09finalgroupproject.databinding.HistoryItemListBinding;
import com.example.team09finalgroupproject.model.Adherence;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    private List<Adherence> adherenceList;
    HistoryItemListBinding itemListBinding;

    public HistoryAdapter(List<Adherence> adherenceList) {
        this.adherenceList = adherenceList;
    }


    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        itemListBinding = HistoryItemListBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(itemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        Adherence adherence = adherenceList.get(position);
        holder.bindView(adherenceList.get(position).getName(), adherenceList.get(position).getDosage(),adherenceList.get(position).getTime(),adherenceList.get(position).getDate(),adherenceList.get(position).getAdherenceStatus());

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        HistoryItemListBinding itemListBinding;

        public ViewHolder(HistoryItemListBinding itemListBinding) {
            super(itemListBinding.getRoot());
            this.itemListBinding = itemListBinding;
        }
        void bindView(final String name,final String dosage,final String time,final String date,final String adherenceStatus){
            itemListBinding.txtMedicationName.setText(name);
            itemListBinding.txtDosage.setText(dosage);
            itemListBinding.txtTime.setText(time);
            itemListBinding.txtDate.setText(date);
            itemListBinding.txtAdherenceStatus.setText(adherenceStatus);

        }
    }

    @Override
    public int getItemCount() {
        return adherenceList.size();
    }

    public void updateList(List<Adherence> newAdherenceList) {
        adherenceList.clear();
        adherenceList.addAll(newAdherenceList);
        notifyDataSetChanged();
    }
}
