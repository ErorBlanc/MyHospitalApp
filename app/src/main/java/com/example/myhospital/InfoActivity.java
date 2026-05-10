package com.example.myhospital;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        findViewById(R.id.btnBackInfo).setOnClickListener(v -> finish());
        String type = getIntent().getStringExtra("type");
        TextView title = findViewById(R.id.tvInfoTitle);
        TextView content = findViewById(R.id.tvInfoText);

        if ("analysis".equals(type)) {
            title.setText("Мои Анализы");
            content.setText("• Общий анализ крови (Готов)\n• Анализ на витамины (В работе)\n• ПЦР-тест (Готов)");
        } else {
            title.setText("Мои Справки");
            content.setText("• Справка в бассейн (Активна)\n• Выписка из карты (Архив)");
        }
    }
}