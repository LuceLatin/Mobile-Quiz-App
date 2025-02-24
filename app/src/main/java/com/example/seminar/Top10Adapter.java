package com.example.seminar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// U Top10Adapter.java
// U Top10Adapter.java
public class Top10Adapter extends RecyclerView.Adapter<Top10Adapter.ViewHolder> {

    private List<Rezultat> rezultati;

    public Top10Adapter(List<Rezultat> rezultati) {
        this.rezultati = rezultati;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_top10, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rezultat rezultat = rezultati.get(position);
        holder.bind(rezultat);
    }

    @Override
    public int getItemCount() {
        return rezultati.size();
    }

    public void setRezultatiList(List<Rezultat> rezultati) {
        this.rezultati = rezultati;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewScore;
        private final TextView textViewEmail;
        private final TextView textViewBrojIgara;
        private final TextView textViewBrojTocnihOdgovora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewScore = itemView.findViewById(R.id.textViewScore);
            textViewEmail = itemView.findViewById(R.id.textViewPlayerName);
            textViewBrojIgara = itemView.findViewById(R.id.textViewBrojIgara);
            textViewBrojTocnihOdgovora = itemView.findViewById(R.id.textViewBrojTocnihOdgovora);
        }

        public void bind(Rezultat rezultat) {
            textViewScore.setText(String.valueOf(rezultat.getBrojTocnihOdgovora())); // Promijenjeno da koristi getBrojTocnihOdgovora
            textViewEmail.setText(rezultat.getName());
            textViewBrojIgara.setText("Broj igara: " + rezultat.getBrojOdigranihIgara());
            textViewBrojTocnihOdgovora.setText("Postotak točnih odgovora: " + rezultat.getScore());
        }
    }
}
