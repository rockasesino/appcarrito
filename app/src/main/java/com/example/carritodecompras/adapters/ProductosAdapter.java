package com.example.carritodecompras.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carritodecompras.DatabaseHelper;
import com.example.carritodecompras.R;
import com.example.carritodecompras.models.Producto;

import java.util.ArrayList;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ViewHolder> {

    Context context;
    ArrayList<Producto> lista;

    public ProductosAdapter(Context context, ArrayList<Producto> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ProductosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto p = lista.get(position);

        holder.txtNombre.setText(p.getNombre());
        holder.txtPrecio.setText("$" + p.getPrecio());

        // Cargar imagen local desde drawable
        int imgRes = context.getResources().getIdentifier(
                p.getImagenUrl(),
                "drawable",
                context.getPackageName()
        );

        if (imgRes != 0) holder.imgProducto.setImageResource(imgRes);
        else holder.imgProducto.setImageResource(R.drawable.ic_placeholder);

        // ----- BOTÓN AÑADIR AL CARRITO -----
        holder.btnAdd.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(context);
            db.agregarAlCarrito(p.getNombre(), p.getPrecio(), p.getImagenUrl());

            holder.btnAdd.animate().rotation(360).setDuration(300);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProducto, btnAdd;
        TextView txtNombre, txtPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProducto = itemView.findViewById(R.id.imgProducto);
            btnAdd = itemView.findViewById(R.id.btnAgregar);
            txtNombre = itemView.findViewById(R.id.txtNombreProducto);
            txtPrecio = itemView.findViewById(R.id.txtPrecioProducto);
        }
    }
}
