package com.example.carritodecompras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.carritodecompras.adapters.ProductosAdapter;
import com.example.carritodecompras.models.Producto;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProductosActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        // -------------------------
        // TOOLBAR
        // -------------------------
        MaterialToolbar toolbar = findViewById(R.id.toolbarProductos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // -------------------------
        // INICIALIZACIÓN
        // -------------------------
        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerProductos);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Producto> productos = db.obtenerProductos();

        recyclerView.setAdapter(new ProductosAdapter(this, productos));

        // -------------------------
        // BOTÓN PARA IR AL CARRITO
        // -------------------------
        FloatingActionButton btnIrCarrito = findViewById(R.id.btnIrCarrito);

        btnIrCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(ProductosActivity.this, CarritoActivity.class);
            startActivity(intent);
        });
    }
}
