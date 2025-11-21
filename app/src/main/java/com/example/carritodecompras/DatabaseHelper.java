package com.example.carritodecompras;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.carritodecompras.models.Producto;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "carrito.db";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Tabla usuarios
        db.execSQL(
                "CREATE TABLE usuarios (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nombre TEXT, " +
                        "email TEXT UNIQUE, " +
                        "password TEXT" +
                        ")"
        );

        // Tabla carrito
        db.execSQL(
                "CREATE TABLE carrito (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nombre TEXT, " +
                        "precio REAL, " +
                        "imagen TEXT" +
                        ")"
        );

        // Tabla productos
        db.execSQL(
                "CREATE TABLE productos (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nombre TEXT, " +
                        "precio REAL, " +
                        "imagen TEXT" +
                        ")"
        );

        // Insertar productos iniciales
        insertarProducto(db, "Cámara Sony", 850.00, "camara_sony");
        insertarProducto(db, "Laptop Lenovo", 1200.00, "laptop_lenovo");
        insertarProducto(db, "Mouse Gamer", 25.50, "mouse_gamer");
        insertarProducto(db, "Teclado Mecánico", 55.00, "teclado_mecanico");
    }

    private void insertarProducto(SQLiteDatabase db, String nombre, double precio, String imagen) {
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("precio", precio);
        values.put("imagen", imagen);
        db.insert("productos", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS carrito");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS productos");
        onCreate(db);
    }

    // ===================================
    //      REGISTRAR USUARIO
    // ===================================
    public boolean registrarUsuario(String nombre, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("email", email);
        values.put("password", password);

        long result = db.insert("usuarios", null, values);
        db.close();

        return result != -1;
    }

    // ===================================
    //     ¿EXISTE EL USUARIO?
    // ===================================
    public boolean usuarioExiste(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM usuarios WHERE email = ?",
                new String[]{email}
        );

        boolean existe = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return existe;
    }

    // ===================================
    //     VALIDAR LOGIN
    // ===================================
    public boolean validarUsuario(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM usuarios WHERE email=? AND password=?",
                new String[]{email, password}
        );

        boolean existe = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return existe;
    }

    // ===================================
    //     CARRITO
    // ===================================
    public void agregarAlCarrito(String nombre, double precio, String imagen) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("precio", precio);
        values.put("imagen", imagen);

        db.insert("carrito", null, values);
        db.close();
    }

    public ArrayList<Producto> obtenerCarrito() {
        ArrayList<Producto> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, nombre, precio, imagen FROM carrito",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                lista.add(new Producto(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }

    public void eliminarDelCarrito(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("carrito", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void vaciarCarrito() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("carrito", null, null);
        db.close();
    }

    public double calcularTotalCarrito() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(precio) FROM carrito", null);

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }

    // ===================================
    //      LISTA DE PRODUCTOS
    // ===================================
    public ArrayList<Producto> obtenerProductos() {
        ArrayList<Producto> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, nombre, precio, imagen FROM productos",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                lista.add(new Producto(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }
}
