package com.example.myhospital;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText regFullName, regEmail, regPhone, regPassword;
    private Button btnRegister;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regFullName = findViewById(R.id.regFullName);
        regEmail = findViewById(R.id.regEmail);
        regPhone = findViewById(R.id.regPhone);
        regPassword = findViewById(R.id.regPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        btnRegister.setOnClickListener(v -> {
            String fullName = regFullName.getText().toString().trim();
            String email = regEmail.getText().toString().trim();
            String phone = regPhone.getText().toString().trim();
            String password = regPassword.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() ||
                    phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,
                        "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getInstance(this);

                User existing = db.userDao().getUserByEmail(email);

                if (existing != null) {
                    runOnUiThread(() ->
                            Toast.makeText(this,
                                    "Этот email уже зарегистрирован",
                                    Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                User newUser = new User();
                newUser.fullName = fullName;
                newUser.email = email;
                newUser.phone = phone;
                newUser.password = password;

                db.userDao().insert(newUser);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("REGISTERED_EMAIL", email);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Закрываем окно регистрации
                });
            });
        });

        tvBackToLogin.setOnClickListener(v -> {
            finish();
        });
    }
}