package com.example.myhospital;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class TimeSelectionActivity extends AppCompatActivity {

    private String selectedDate;
    private String selectedTime = "";
    private int docId;

    private TextView tvStatus;
    private ChipGroup chipGroup;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_selection);

        docId = getIntent().getIntExtra("DOC_ID", -1);
        String docName = getIntent().getStringExtra("DOC_NAME");
        String docSpec = getIntent().getStringExtra("DOC_SPEC");

        // ИСПРАВЛЕНИЕ 1: Добавлена точка с запятой в конце!
        int userId = getSharedPreferences("MyHospital", MODE_PRIVATE).getInt("userId", 1);

        TextView tvName = findViewById(R.id.tvSelectedDoctorName);
        TextView tvSpec = findViewById(R.id.tvSelectedDoctorSpec);

        tvStatus = findViewById(R.id.tvWorkStatus);
        chipGroup = findViewById(R.id.chipGroupTime);
        btnConfirm = findViewById(R.id.btnConfirmAppointment);
        CalendarView calendarView = findViewById(R.id.calendarView);

        tvName.setText(docName);
        tvSpec.setText(docSpec);

        // Устанавливаем текущую дату
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        selectedDate = sdf.format(cal.getTime());

        // ИСПРАВЛЕНИЕ 2: Передаем docSpec, чтобы приложение знало, врач это или Лаборатория
        checkDayAndUpdateUI(cal, docSpec);

        // Слушатель изменения даты в календаре
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            selectedDate = String.format(Locale.getDefault(), "%02d.%02d.%d", dayOfMonth, month + 1, year);

            checkDayAndUpdateUI(selected, docSpec);
        });

        btnConfirm.setOnClickListener(v -> {
            int checkedId = chipGroup.getCheckedChipId();
            if (checkedId != -1) {
                selectedTime = ((Chip) findViewById(checkedId)).getText().toString();
                saveAppointment(userId, docId, docName, docSpec);
            } else {
                Toast.makeText(this, "Выберите время", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnBackTime).setOnClickListener(v -> finish());
    }

    // ИСПРАВЛЕНИЕ 3: Вернули логику графика (2/2 для врачей, Пн-Сб для Лаборатории)
    private void checkDayAndUpdateUI(Calendar date, String docSpec) {
        boolean isWorkingDay;

        if ("Лаборатория".equals(docSpec)) {
            // Лаборатория работает каждый день кроме воскресенья
            int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
            isWorkingDay = (dayOfWeek != Calendar.SUNDAY);
        } else {
            // Обычные врачи работают по графику 2 через 2
            int dayOfYear = date.get(Calendar.DAY_OF_YEAR);
            isWorkingDay = (dayOfYear + docId) % 4 < 2;
        }

        if (isWorkingDay) {
            tvStatus.setVisibility(View.GONE);
            chipGroup.setVisibility(View.VISIBLE);
            btnConfirm.setVisibility(View.VISIBLE);
            updateTimeSlots(chipGroup, docSpec);
        } else {
            tvStatus.setVisibility(View.VISIBLE);
            chipGroup.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
        }
    }

    private void updateTimeSlots(ChipGroup group, String spec) {
        group.removeAllViews();
        String[] hours;

        if ("Лаборатория".equals(spec)) {
            hours = new String[]{"09:00", "10:00", "11:00", "12:00", "13:00"};
        } else {
            hours = (docId % 2 == 0) ?
                    new String[]{"09:00", "10:00", "11:00", "12:00"} :
                    new String[]{"14:00", "15:00", "16:00", "17:00"};
        }

        for (String h : hours) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_time_chip, group, false);
            chip.setText(h);
            group.addView(chip);
        }
    }

    private void saveAppointment(int uId, int dId, String name, String spec) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            Appointment app = new Appointment();
            app.userId = uId;
            app.doctorId = dId;
            app.doctorName = name;
            app.specialization = spec;
            app.date = selectedDate;
            app.time = selectedTime;
            app.status = "upcoming";

            db.appointmentDao().insert(app);

            runOnUiThread(() -> {
                Toast.makeText(this, "Запись подтверждена!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        });
    }
}