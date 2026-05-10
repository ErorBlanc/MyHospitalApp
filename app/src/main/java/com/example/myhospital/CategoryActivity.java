package com.example.myhospital;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Кнопка назад
        findViewById(R.id.btnBackCategory).setOnClickListener(v -> finish());

        // 1. Блок "Симптомы"
        setupClick(R.id.cardSymptomFeelBad, "Терапевт");
        setupClick(R.id.cardSymptomOrvi, "Терапевт");
        setupClick(R.id.cardSymptomTemp, "Терапевт");
        setupClick(R.id.cardSymptomTooth, "Стоматолог");
        setupClick(R.id.cardSymptomHeadache, "Невролог");
        setupClick(R.id.cardSymptomStomach, "Гастроэнтеролог");

        // 2. Блок "Помощь специалиста"
        // Передаем пустую строку "", чтобы AppointmentActivity показал всех врачей
        setupClick(R.id.cardSpecTherapist, "");

        // Для остальных передаем конкретную специализацию
        setupClick(R.id.cardSpecSurgeon, "Стоматолог");
        setupClick(R.id.cardSpecOphthalmologist, "Офтальмолог");
        setupClick(R.id.cardSpecPsychologist, "Психолог");

        // 3. Блок "Прочее" (Анализы и справки)
        // Пока тоже будут открывать общий список (или можешь сделать для них отдельные экраны позже)
        setupClick(R.id.cardActionAnalysis, "Лаборатория");
        setupClick(R.id.cardActionCertificates, "Лаборатория");
    }

    private void setupClick(int viewId, String spec) {
        findViewById(viewId).setOnClickListener(v -> {
            Intent intent = new Intent(
                    CategoryActivity.this,
                    AppointmentActivity.class
            );
            // Передаем фильтр (или пустую строку) на следующий экран
            intent.putExtra("SPECIALIZATION", spec);
            startActivity(intent);
        });
    }
}