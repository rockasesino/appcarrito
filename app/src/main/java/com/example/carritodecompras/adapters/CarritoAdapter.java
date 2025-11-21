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

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    Context context;
    ArrayList<Producto> lista;
    Runnable actualizarTotal;

    public CarritoAdapter(Context context, ArrayList<Producto> lista, Runnable actualizarTotal) {
        this.context = context;
        this.lista = lista;
        this.actualizarTotal = actualizarTotal;
    }

    @NonNull
    @Override
    public CarritoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto p = lista.get(position);

        holder.txtNombre.setText(p.getNombre());
        holder.txtPrecio.setText("$" + p.getPrecio());

        // --- CARGAR IMAGEN LOCAL ---
        int imgRes = context.getResources().getIdentifier(
                p.getImagenUrl(), // ej: "audifonos"
                "drawable",
                context.getPackageName()
        );

        if (imgRes != 0) {
            holder.imgProducto.setImageResource(imgRes);
        } else {
            holder.imgProducto.setImageResource(R.drawable.ic_placeholder);
        }

        // Botón eliminar dentro de cada item
        holder.btnEliminar.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(context);
            db.eliminarDelCarrito(p.getId());

            lista.remove(position);
            notifyItemRemoved(position);

            actualizarTotal.run();
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProducto, btnEliminar;
        TextView txtNombre, txtPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProducto = itemView.findViewById(R.id.imgProductoCarrito);
            btnEliminar = itemView.findViewById(R.id.btnEliminarItem);
            txtNombre = itemView.findViewById(R.id.txtNombreCarrito);
            txtPrecio = itemView.findViewById(R.id.txtPrecioCarrito);
        }
    }
}
