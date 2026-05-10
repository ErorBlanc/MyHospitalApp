package com.example.myhospital;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    private int userId;
    private TextView tvUpcomingDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 1. Загрузка данных пользователя (Имя теперь не пропадает)
        userId = getSharedPreferences("MyHospital", MODE_PRIVATE).getInt("userId", 1);
        String fullName = getSharedPreferences("MyHospital", MODE_PRIVATE).getString("userName", "Пользователь");

        TextView tvUserName = findViewById(R.id.tvUserNameHome);
        tvUserName.setText(fullName.split(" ")[0]);

        tvUpcomingDetails = findViewById(R.id.tvUpcomingDetails);
        Button btnMainAppointment = findViewById(R.id.btnMainAppointment);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);

        // 2. Логика кнопок записи
        btnMainAppointment.setOnClickListener(v -> startActivity(new Intent(this, CategoryActivity.class)));

        // Быстрая запись через фильтр
        findViewById(R.id.cardQuickTemp).setOnClickListener(v -> openFilteredDocs("Терапевт"));
        findViewById(R.id.cardQuickFeelBad).setOnClickListener(v -> openFilteredDocs("Терапевт"));
        findViewById(R.id.cardQuickHistory).setOnClickListener(v ->
                startActivity(new Intent(this, MyAppointmentsActivity.class)));

        // 3. Анализы и Справки (Наш новый экран-заглушка)
        findViewById(R.id.cardActionAnalysisHome).setOnClickListener(v -> openInfo("analysis"));
        findViewById(R.id.cardActionCertsHome).setOnClickListener(v -> openInfo("certs"));

        // 4. Навигация
        bottomNavigation.setSelectedItemId(R.id.nav_home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_add) {
                startActivity(new Intent(this, CategoryActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {
                // Выход
                getSharedPreferences("MyHospital", MODE_PRIVATE).edit().clear().apply();
                startActivity(new Intent(this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;
            }
            return true; // Если нажата главная (nav_home), просто остаемся тут
        });

        loadUpcoming();
    }

    private void openFilteredDocs(String spec) {
        Intent intent = new Intent(this, AppointmentActivity.class);
        intent.putExtra("SPECIALIZATION", spec);
        startActivity(intent);
    }

    private void openInfo(String type) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void loadUpcoming() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            List<Appointment> list = db.appointmentDao().getUpcomingAppointments(userId);
            runOnUiThread(() -> {
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
    protected void onResume() {
        super.onResume();
        // Обновляем данные о ближайшем приеме
        loadUpcoming();

        // Принудительно возвращаем подсветку на кнопку "Главная"
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        if (bottomNavigation.getSelectedItemId() != R.id.nav_home) {
            bottomNavigation.setSelectedItemId(R.id.nav_home);
        }
    }
}