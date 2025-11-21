package com.example.carritodecompras;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnRegistro, btnProductos, btnCarrito, btnUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajuste de insets (se mantiene igual)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Vinculación de botones
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistro = findViewById(R.id.btnRegistro);
        btnProductos = findViewById(R.id.btnProductos);
        btnCarrito = findViewById(R.id.btnCarrito);
        btnUbicacion = findViewById(R.id.btnUbicacion);

        // Animación Fade-In al iniciar pantalla
        View mainLayout = findViewById(R.id.main);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.tech_fade_in);
        mainLayout.startAnimation(fadeIn);

        // -------------------------------
        // ANIMACIONES DE BOTONES (pulse)
        // -------------------------------

        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.btn_pulse);

        // LOGIN
        btnLogin.setOnClickListener(view -> {
            view.startAnimation(pulse);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        // REGISTRO
        btnRegistro.setOnClickListener(view -> {
            view.startAnimation(pulse);
            startActivity(new Intent(MainActivity.this, RegistroActivity.class));
        });

        // PRODUCTOS
        btnProductos.setOnClickListener(view -> {
            view.startAnimation(pulse);
            startActivity(new Intent(MainActivity.this, ProductosActivity.class));
        });

        // CARRITO
        btnCarrito.setOnClickListener(view -> {
            view.startAnimation(pulse);
            startActivity(new Intent(MainActivity.this, CarritoActivity.class));
        });

        // UBICACIÓN
        btnUbicacion.setOnClickListener(view -> {
            view.startAnimation(pulse);
            startActivity(new Intent(MainActivity.this, UbicacionActivity.class));
        });
    }
}
