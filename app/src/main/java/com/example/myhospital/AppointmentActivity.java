package com.example.myhospital;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AppointmentActivity extends AppCompatActivity {

    private DoctorAdapter adapter;
    private List<Doctor> allDoctors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        ImageView btnBack = findViewById(R.id.btnBack);
        RecyclerView recycler = findViewById(R.id.recyclerViewDoctors);

        recycler.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        loadDoctors(recycler);
    }

    private void loadDoctors(RecyclerView recycler) {
        String filterSpec = getIntent().getStringExtra("SPECIALIZATION");

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            if (db.doctorDao().getAllDoctors().isEmpty()) {
                insertDummyDoctors(db);
            }

            if (filterSpec != null && !filterSpec.isEmpty()) {
                allDoctors = db.doctorDao().getDoctorsBySpecialization(filterSpec);
            } else {
                allDoctors = db.doctorDao().getAllDoctors();
            }

            runOnUiThread(() -> {
                adapter = new DoctorAdapter(d -> {
                    Intent intent = new Intent(AppointmentActivity.this, TimeSelectionActivity.class);
                    intent.putExtra("DOC_ID", d.id);
                    intent.putExtra("DOC_NAME", d.name);
                    intent.putExtra("DOC_SPEC", d.specialization);
                    startActivity(intent);
                });
                recycler.setAdapter(adapter);
                adapter.submitList(allDoctors);
            });
        });
    }

    private void insertDummyDoctors(AppDatabase db) {
        List<Doctor> dummies = new ArrayList<>();

        // Терапевты
        dummies.add(createDoc("Ivanov Ivan", "Терапевт", "101"));
        dummies.add(createDoc("Smirnova Anna", "Терапевт", "102"));

        // Офтальмологи
        dummies.add(createDoc("Petrov Petr", "Офтальмолог", "205"));
        dummies.add(createDoc("Sokolov Alexey", "Офтальмолог", "206"));

        // Хирурги
        dummies.add(createDoc("Vasiliev Andrey", "Хирург", "302"));
        dummies.add(createDoc("Morozova Elena", "Хирург", "303"));

        // Стоматологи Зубная боль
        dummies.add(createDoc("Fedorov Dmitry", "Стоматолог", "401"));
        dummies.add(createDoc("Kuznetsova Maria", "Стоматолог", "402"));

        // Невролог Головная боль
        dummies.add(createDoc("Sidorov Ivan", "Невролог", "505"));

        // Гастроэнтеролог Боль в животе
        dummies.add(createDoc("Volkova Olga", "Гастроэнтеролог", "601"));

        // Психолог
        dummies.add(createDoc("Popov Sergey", "Психолог", "701"));
        dummies.add(createDoc("Кабинет забора крови", "Лаборатория", "К-101"));
        dummies.add(createDoc("Кабинет приема ЭКГ", "Лаборатория", "Э-202"));
        dummies.add(createDoc("Отдел выдачи справок", "Справки", "С-303"));

        db.doctorDao().insertAll(dummies);
    }

    private Doctor createDoc(String name, String spec, String cab) {
        Doctor d = new Doctor();
        d.name = name;
        d.specialization = spec;
        d.cabinet = cab;
        return d;
    }
}