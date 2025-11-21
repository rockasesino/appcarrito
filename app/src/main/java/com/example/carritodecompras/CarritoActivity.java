package com.example.carritodecompras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.carritodecompras.adapters.CarritoAdapter;
import com.example.carritodecompras.models.Producto;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class CarritoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txtTotal;
    CarritoAdapter adapter;
    DatabaseHelper db;
    ArrayList<Producto> carrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        // ---------------------------
        // CONFIGURACIÓN DE TOOLBAR
        // ---------------------------
        MaterialToolbar toolbar = findViewById(R.id.toolbarCarrito);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        // ---------------------------
        // INICIALIZACIÓN
        // ---------------------------
        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerCarrito);
        txtTotal = findViewById(R.id.txtTotal);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        carrito = db.obtenerCarrito(); // Se guarda para actualizar

        adapter = new CarritoAdapter(this, carrito, this::actualizarTotal);
        recyclerView.setAdapter(adapter);

        actualizarTotal();

        // ---------------------------
        // BOTÓN: Vaciar carrito (con confirmación)
        // ---------------------------
        findViewById(R.id.btnVaciar).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Vaciar carrito")
                    .setMessage("¿Estás seguro de que deseas eliminar todos los productos?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        db.vaciarCarrito();
                        carrito.clear();
                        adapter.notifyDataSetChanged();
                        actualizarTotal();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // ---------------------------
        // BOTÓN: Volver
        // ---------------------------
        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());

        // ---------------------------
        // BOTÓN: Ver ubicación
        // ---------------------------
        findViewById(R.id.btnVerUbicacion).setOnClickListener(v -> {
            Intent intent = new Intent(CarritoActivity.this, UbicacionActivity.class);
            startActivity(intent);
        });


        // ---------------------------
        // SWIPE PARA ELIMINAR ELEMENTO
        // ---------------------------
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Producto p = carrito.get(position);

                db.eliminarDelCarrito(p.getId());
                carrito.remove(position);
                adapter.notifyItemRemoved(position);

                actualizarTotal();
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }

    // ---------------------------
    // ACTUALIZAR TOTAL (con animación)
    // ---------------------------
    private void actualizarTotal() {
        double total = db.calcularTotalCarrito();

        txtTotal.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(150)
                .withEndAction(() -> {
                    txtTotal.setText("Total: $" + total);
                    txtTotal.animate().scaleX(1f).scaleY(1f).setDuration(150);
                });
    }
}
