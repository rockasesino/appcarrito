package com.example.carritodecompras;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carritodecompras.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DatabaseHelper(this);

        binding.cardLogin.startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.fade_in)
        );

        binding.btnLogin.setOnClickListener(v -> validarLogin());

        binding.btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    LoginActivity.this,
                    Pair.create(binding.cardLogin, "transicion_login")
            );

            startActivity(intent, options.toBundle());
        });
    }

    private void validarLogin() {

        String email = binding.etEmail.getText().toString().trim();
        String pass = binding.etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            binding.inputEmail.setError("Ingresa tu email");
            return;
        }

        if (pass.isEmpty()) {
            binding.inputPassword.setError("Ingresa tu contraseña");
            return;
        }

        boolean valido = db.validarUsuario(email, pass);

        if (valido) {
            Toast.makeText(this, "Bienvenido!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, ProductosActivity.class);
            overridePendingTransition(R.anim.deslizar_derecha, R.anim.desvanecer);
            startActivity(intent);

        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }
}
