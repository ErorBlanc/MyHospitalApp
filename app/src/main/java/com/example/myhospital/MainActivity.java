package com.example.myhospital;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private Button loginBtn;
    private TextView registerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        registerTv = findViewById(R.id.registerTv);

        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,
                        "Введите email и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            // Проверка в БД в отдельном потоке
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getInstance(this);
                User user = db.userDao().login(email, password);

                runOnUiThread(() -> {
                    if (user != null) {
                        // --- ИЗМЕНЕНИЕ ЗДЕСЬ: Сохраняем данные пользователя ---
                        // Записываем имя и ID в локальное хранилище телефона
                        getSharedPreferences("MyHospital", MODE_PRIVATE)
                                .edit()
                                .putString("userName", user.fullName)
                                .putInt("userId", user.id)
                                .apply();
                        // ------------------------------------------------------

                        // Успешный вход
                        Intent intent = new Intent(
                                MainActivity.this, HomeActivity.class);
                        // Для надежности все еще передаем через Intent
                        intent.putExtra("userId", user.id);
                        intent.putExtra("userName", user.fullName);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this,
                                "Неверный email или пароль",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        // Переход на регистрацию
        registerTv.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}