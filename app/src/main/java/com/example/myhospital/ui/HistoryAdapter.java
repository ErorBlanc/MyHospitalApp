package com.example.myhospital.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.myhospital.R;
import com.example.myhospital.model.Appointment;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<Appointment> appointmentList;

    public HistoryAdapter(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_card, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);

        holder.tvSpec.setText(appointment.specialization);
        holder.tvDocName.setText(appointment.doctorName);
        holder.tvDateTime.setText(appointment.date + " в " + appointment.time);
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvSpec, tvDocName, tvDateTime;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSpec = itemView.findViewById(R.id.tvHistorySpec);
            tvDocName = itemView.findViewById(R.id.tvHistoryDocName);
            tvDateTime = itemView.findViewById(R.id.tvHistoryDateTime);
        }
    }
}