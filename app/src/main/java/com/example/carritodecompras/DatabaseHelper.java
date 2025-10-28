package com.example.carritodecompras;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "carrito_compras.db";
    private static final int DB_VERSION = 2;

    // Tablas
    private static final String TABLA_PRODUCTOS = "productos";
    private static final String TABLA_CARRITO = "carrito";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLA_PRODUCTOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "precio REAL)");

        db.execSQL("CREATE TABLE " + TABLA_CARRITO + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "producto TEXT, " +
                "precio REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CARRITO);
        onCreate(db);
    }

    // ---------- CRUD Productos ----------
    public boolean agregarProducto(String nombre, double precio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("precio", precio);
        long result = db.insert(TABLA_PRODUCTOS, null, values);
        return result != -1;
    }

    public ArrayList<Producto> obtenerProductos() {
        ArrayList<Producto> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_PRODUCTOS, null);
        if (cursor.moveToFirst()) {
            do {
                lista.add(new Producto(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public boolean actualizarProducto(int id, String nombre, double precio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("precio", precio);
        int rows = db.update(TABLA_PRODUCTOS, values, "id=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean eliminarProducto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLA_PRODUCTOS, "id=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    // ---------- CRUD Carrito ----------
    public boolean agregarAlCarrito(String producto, double precio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("producto", producto);
        values.put("precio", precio);
        long result = db.insert(TABLA_CARRITO, null, values);
        return result != -1;
    }

    public ArrayList<Producto> obtenerCarrito() {
        ArrayList<Producto> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_CARRITO, null);
        if (cursor.moveToFirst()) {
            do {
                lista.add(new Producto(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public boolean eliminarDelCarrito(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLA_CARRITO, "id=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public double calcularTotalCarrito() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(precio) FROM " + TABLA_CARRITO, null);
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    public void vaciarCarrito() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_CARRITO, null, null);
    }
}
