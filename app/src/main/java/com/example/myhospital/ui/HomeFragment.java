package com.example.myhospital.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.example.myhospital.data.AppDatabase;
import com.example.myhospital.model.Appointment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.List;
import java.util.concurrent.Executors;
import com.example.myhospital.R;

public class HomeFragment extends Fragment {

    private int userId;
    private TextView tvUpcomingDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (getActivity() != null) {
            userId = getActivity().getSharedPreferences("MyHospital", Context.MODE_PRIVATE).getInt("userId", 1);
            String fullName = getActivity().getSharedPreferences("MyHospital", Context.MODE_PRIVATE).getString("userName", "Пользователь");

            TextView tvUserName = view.findViewById(R.id.tvUserNameHome);

            String[] nameParts = fullName.split(" ");
            if (nameParts.length > 1) {
                tvUserName.setText(nameParts[1]);
            } else {
                tvUserName.setText(fullName);
            }
        }

        tvUpcomingDetails = view.findViewById(R.id.tvUpcomingDetails);
        Button btnMainAppointment = view.findViewById(R.id.btnMainAppointment);

        btnMainAppointment.setOnClickListener(v -> startActivity(new Intent(getActivity(), CategoryActivity.class)));

        view.findViewById(R.id.cardQuickTemp).setOnClickListener(v -> openFilteredDocs("Терапевт"));
        view.findViewById(R.id.cardQuickFeelBad).setOnClickListener(v -> openFilteredDocs("Терапевт"));
        view.findViewById(R.id.cardQuickHistory).setOnClickListener(v -> startActivity(new Intent(getActivity(), MyAppointmentsActivity.class)));

        view.findViewById(R.id.cardActionAnalysisHome).setOnClickListener(v -> openInfo("analysis"));
        view.findViewById(R.id.cardActionCertsHome).setOnClickListener(v -> openInfo("certs"));

        loadUpcoming();

        return view;
    }

    private void openFilteredDocs(String spec) {
        Intent intent = new Intent(getActivity(), AppointmentActivity.class);
        intent.putExtra("SPECIALIZATION", spec);
        startActivity(intent);
    }

    private void openInfo(String type) {
        Intent intent = new Intent(getActivity(), InfoActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void loadUpcoming() {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (getActivity() == null) return;
            AppDatabase db = AppDatabase.getInstance(getActivity());
            List<Appointment> list = db.appointmentDao().getUpcomingAppointments(userId);

            getActivity().runOnUiThread(() -> {
                if (!list.isEmpty()) {
                    Appointment a = list.get(0);
                    tvUpcomingDetails.setText(a.specialization + " • " + a.date + " в " + a.time);
                } else {
                    tvUpcomingDetails.setText("У вас пока нет активных записей");
                }
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUpcoming();
    }
}