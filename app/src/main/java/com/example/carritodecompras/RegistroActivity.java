package com.example.carritodecompras;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carritodecompras.databinding.ActivityRegistroBinding;

public class RegistroActivity extends AppCompatActivity {

    ActivityRegistroBinding binding;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DatabaseHelper(this);

        binding.btnRegistrar.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {

        String nombre = binding.etNombre.getText().toString().trim();
        String email = binding.etCorreo.getText().toString().trim();
        String pass = binding.etPassword.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // ¿Ya existe el email?
        if (db.usuarioExiste(email)) {
            Toast.makeText(this, "El correo ya está registrado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registrar en DB (AQUÍ ESTABA EL ERROR)
        boolean registrado = db.registrarUsuario(nombre, email, pass);

        if (registrado) {
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
        }
    }
}
