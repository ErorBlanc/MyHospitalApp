package com.example.myhospital;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MyAppointmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);

        // Кнопка назад
        findViewById(R.id.btnBackHistory).setOnClickListener(v -> finish());

        ListView listView = findViewById(R.id.listViewHistory);
        int userId = getSharedPreferences("MyHospital", MODE_PRIVATE).getInt("userId", 1);

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            // Получаем ВСЕ записи пользователя из базы (метод уже есть в твоем AppointmentDao)
            List<Appointment> appointments = db.appointmentDao().getAppointmentsForUser(userId);

            // Превращаем сложный объект Appointment в простой текст
            List<String> displayList = new ArrayList<>();
            for (Appointment a : appointments) {
                String record = a.specialization + " • " + a.doctorName + "\n" + a.date + " в " + a.time;
                displayList.add(record);
            }

            // Если записей нет
            if (displayList.isEmpty()) {
                displayList.add("У вас пока нет истории приемов.");
            }

            // Выводим на экран через стандартный адаптер Android (1 строчка текста на элемент)
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1, // Встроенный шаблон Android!
                        displayList
                );
                listView.setAdapter(adapter);
            });
        });
    }
}