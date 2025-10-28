package com.example.carritodecompras;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CarritoActivity extends AppCompatActivity {

    DatabaseHelper db;
    ListView listaCarrito;
    TextView txtTotal;
    Button btnEliminar, btnVaciar, btnVolver, btnVerUbicacion;

    ArrayList<Producto> lista;
    ArrayAdapter<String> adaptador;
    Producto productoSeleccionado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        db = new DatabaseHelper(this);

        // Referencias
        listaCarrito = findViewById(R.id.listaCarrito);
        txtTotal = findViewById(R.id.txtTotal);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVaciar = findViewById(R.id.btnVaciar);
        btnVolver = findViewById(R.id.btnVolver);
        btnVerUbicacion = findViewById(R.id.btnVerUbicacion);

        cargarCarrito();

        // Seleccionar producto
        listaCarrito.setOnItemClickListener((adapterView, view, i, l) -> {
            productoSeleccionado = lista.get(i);
        });

        // Eliminar producto
        btnEliminar.setOnClickListener(v -> {
            if (productoSeleccionado == null) {
                Toast.makeText(this, "Selecciona un producto del carrito", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Eliminar producto")
                    .setMessage("¿Deseas eliminar este producto del carrito?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        db.eliminarDelCarrito(productoSeleccionado.getId());
                        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                        productoSeleccionado = null;
                        cargarCarrito();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Vaciar carrito
        btnVaciar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Vaciar carrito")
                    .setMessage("¿Deseas eliminar todos los productos?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        db.vaciarCarrito();
                        Toast.makeText(this, "Carrito vaciado", Toast.LENGTH_SHORT).show();
                        cargarCarrito();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Volver
        btnVolver.setOnClickListener(v -> {
            startActivity(new Intent(CarritoActivity.this, ProductosActivity.class));
            finish();
        });

        // Ver ubicación
        btnVerUbicacion.setOnClickListener(v -> {
            Intent intent = new Intent(CarritoActivity.this, UbicacionActivity.class);
            startActivity(intent);
        });
    }

    // Cargar carrito
    private void cargarCarrito() {
        lista = db.obtenerCarrito();
        ArrayList<String> nombres = new ArrayList<>();

        for (Producto p : lista) {
            nombres.add(p.getNombre() + " - $" + p.getPrecio());
        }

        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombres);
        listaCarrito.setAdapter(adaptador);

        double total = db.calcularTotalCarrito();
        txtTotal.setText("Total: $" + total);
    }
}



