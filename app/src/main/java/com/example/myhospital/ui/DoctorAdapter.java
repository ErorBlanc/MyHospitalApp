package com.example.myhospital.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.myhospital.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myhospital.model.Doctor;

import java.util.List;

public class DoctorAdapter extends ListAdapter<Doctor, DoctorAdapter.DoctorViewHolder> {

    private OnDoctorClickListener listener;

    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }

    public DoctorAdapter(OnDoctorClickListener listener) {
        super(new DoctorDiffCallback());
        this.listener = listener;
    }

    public void setFilteredList(List<Doctor> filteredList) {
        submitList(filteredList);
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = getItem(position);

        holder.tvName.setText(doctor.name);
        holder.tvSpec.setText(doctor.specialization);
        holder.tvCabinet.setText(doctor.cabinet);

        holder.btnSelect.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDoctorClick(doctor);
            }
        });
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSpec, tvCabinet;
        Button btnSelect;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvDoctorName);
            tvSpec = itemView.findViewById(R.id.tvDoctorSpec);
            tvCabinet = itemView.findViewById(R.id.tvDoctorCabinet);
            btnSelect = itemView.findViewById(R.id.btnSelectDoctor);
        }
    }

    static class DoctorDiffCallback extends DiffUtil.ItemCallback<Doctor> {
        @Override
        public boolean areItemsTheSame(@NonNull Doctor oldItem, @NonNull Doctor newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Doctor oldItem, @NonNull Doctor newItem) {
            return oldItem.name.equals(newItem.name) &&
                    oldItem.specialization.equals(newItem.specialization);
        }
    }
}