package com.example.myhospital;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyAppointmentsActivity extends AppCompatActivity {

    private HistoryViewModel historyViewModel;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);

        findViewById(R.id.btnBackHistory).setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int userId = getSharedPreferences("MyHospital", MODE_PRIVATE).getInt("userId", 1);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        historyViewModel.getUserHistory(userId).observe(this, appointments -> {
            if (adapter == null) {
                adapter = new HistoryAdapter(appointments);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        });
    }
}