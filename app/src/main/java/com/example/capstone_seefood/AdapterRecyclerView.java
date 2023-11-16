package com.example.capstone_seefood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> {

    ArrayList<MenuModel> dataMenu;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textNamaMakanan;
        TextView textHargaMakanan;
        ImageView GambarMakanan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textNamaMakanan = itemView.findViewById(R.id.text_nama_makanan);
            textHargaMakanan = itemView.findViewById(R.id.text_harga_makanan);
            GambarMakanan = itemView.findViewById(R.id.image_makanan);
        }
    }

    AdapterRecyclerView(ArrayList<MenuModel> data)
    {
        this.dataMenu = data;
    }
    @NonNull
    @Override
    public AdapterRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_menu_makanan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerView.ViewHolder holder, int position) {

        TextView text_nama_makanan = holder.textNamaMakanan;
        TextView text_harga_makanan = holder.textHargaMakanan;
        ImageView image_makanan = holder.GambarMakanan;

        text_nama_makanan.setText(dataMenu.get(position).getName());
        text_harga_makanan.setText(dataMenu.get(position).getPrice());
        image_makanan.setImageResource(dataMenu.get(position).getMakanan());
    }

    @Override
    public int getItemCount() {
        return dataMenu.size();
    }

}
