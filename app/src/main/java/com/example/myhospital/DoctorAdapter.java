package com.example.myhospital; // Проверь, чтобы пакет совпадал с твоим

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<Doctor> doctorList;
    private OnDoctorClickListener listener;

    // Интерфейс для обработки нажатия на кнопку "Выбрать"
    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }

    // Конструктор адаптера
    public DoctorAdapter(List<Doctor> doctorList, OnDoctorClickListener listener) {
        this.doctorList = doctorList;
        this.listener = listener;
    }

    public void setFilteredList(List<Doctor> filteredList) {
        this.doctorList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Здесь мы указываем наш шаблон карточки (item_doctor)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        // Достаем врача по его позиции в списке
        Doctor doctor = doctorList.get(position);

        // Заполняем текстовые поля данными из базы
        holder.tvName.setText(doctor.name);
        holder.tvSpec.setText(doctor.specialization);
        holder.tvCabinet.setText(doctor.cabinet);

        // Вешаем слушатель клика на кнопку "Выбрать"
        holder.btnSelect.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDoctorClick(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size(); // Сообщаем списку, сколько всего элементов
    }

    // Класс, который "держит" ссылки на элементы нашей карточки
    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSpec, tvCabinet;
        Button btnSelect;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            // Привязываем переменные к ID из item_doctor.xml
            tvName = itemView.findViewById(R.id.tvDoctorName);
            tvSpec = itemView.findViewById(R.id.tvDoctorSpec);
            tvCabinet = itemView.findViewById(R.id.tvDoctorCabinet);
            btnSelect = itemView.findViewById(R.id.btnSelectDoctor);
        }
    }
}