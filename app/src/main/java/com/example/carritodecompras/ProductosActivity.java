package com.example.carritodecompras;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ProductosActivity extends AppCompatActivity {

    EditText txtNombre, txtPrecio;
    Button btnAgregar, btnActualizar, btnEliminar, btnVerCarrito, btnAgregarCarrito;
    ListView listaProductos;

    DatabaseHelper db;
    ArrayList<Producto> productos;
    ArrayAdapter<String> adapter;
    Producto productoSeleccionado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        txtNombre = findViewById(R.id.txtNombre);
        txtPrecio = findViewById(R.id.txtPrecio);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVerCarrito = findViewById(R.id.btnVerCarrito);
        btnAgregarCarrito = findViewById(R.id.btnAgregarCarrito);
        listaProductos = findViewById(R.id.listaProductos);

        db = new DatabaseHelper(this);
        cargarLista();

        // ---- Agregar producto ----
        btnAgregar.setOnClickListener(v -> {
            String nombre = txtNombre.getText().toString();
            String precioStr = txtPrecio.getText().toString();

            if (nombre.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio = Double.parseDouble(precioStr);
            if (db.agregarProducto(nombre, precio)) {
                Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                cargarLista();
            }
        });

        // ---- Actualizar producto ----
        btnActualizar.setOnClickListener(v -> {
            if (productoSeleccionado != null) {
                String nombre = txtNombre.getText().toString();
                double precio = Double.parseDouble(txtPrecio.getText().toString());
                if (db.actualizarProducto(productoSeleccionado.getId(), nombre, precio)) {
                    Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    cargarLista();
                }
            } else {
                Toast.makeText(this, "Selecciona un producto", Toast.LENGTH_SHORT).show();
            }
        });

        // ---- Eliminar producto ----
        btnEliminar.setOnClickListener(v -> {
            if (productoSeleccionado != null) {
                if (db.eliminarProducto(productoSeleccionado.getId())) {
                    Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    cargarLista();
                }
            } else {
                Toast.makeText(this, "Selecciona un producto", Toast.LENGTH_SHORT).show();
            }
        });

        // ---- Seleccionar producto de la lista ----
        listaProductos.setOnItemClickListener((adapterView, view, i, l) -> {
            productoSeleccionado = productos.get(i);
            txtNombre.setText(productoSeleccionado.getNombre());
            txtPrecio.setText(String.valueOf(productoSeleccionado.getPrecio()));
        });

        // ---- Agregar al carrito ----
        btnAgregarCarrito.setOnClickListener(v -> {
            if (productoSeleccionado != null) {
                boolean resultado = db.agregarAlCarrito(
                        productoSeleccionado.getNombre(),
                        productoSeleccionado.getPrecio()
                );
                if (resultado) {
                    Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al agregar al carrito", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Selecciona un producto primero", Toast.LENGTH_SHORT).show();
            }
        });

        // ---- Ir al carrito ----
        btnVerCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(ProductosActivity.this, CarritoActivity.class);
            startActivity(intent);
        });

    }

    private void cargarLista() {
        productos = db.obtenerProductos();
        ArrayList<String> nombres = new ArrayList<>();
        for (Producto p : productos) {
            nombres.add(p.getNombre() + " - $" + p.getPrecio());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombres);
        listaProductos.setAdapter(adapter);
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        productoSeleccionado = null;
    }
}
