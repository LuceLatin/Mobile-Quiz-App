package com.example.seminar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KorisnikoviRezultatiAdapter extends RecyclerView.Adapter<KorisnikoviRezultatiAdapter.ViewHolder> {
    private List<Rezultat> rezultatiList;

    public void setRezultatiList(List<Rezultat> rezultatiList) {
        this.rezultatiList = rezultatiList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_rezultati, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rezultat rezultat = rezultatiList.get(position);
        holder.bind(rezultat);
    }

    @Override
    public int getItemCount() {
        return rezultatiList != null ? rezultatiList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView rezultatTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rezultatTextView = itemView.findViewById(R.id.rezultatTextView);
        }

        void bind(Rezultat rezultat) {
            String rezultatString = "Rezultat: " + rezultat.getBrojTocnihOdgovora() + "/" + rezultat.getUkupnoPitanja() +
                    "\nPostotak točnosti: " + (int) ((float) rezultat.getBrojTocnihOdgovora() / rezultat.getUkupnoPitanja() * 100) + "%" +
                    "\n Preostalo vrijeme: " + String.format("%02d:%02d", rezultat.getPreostaloVrijeme()  /1000 / 60 , (rezultat.getPreostaloVrijeme()/1000) %60) +
                    "/" + String.format("%02d:%02d", rezultat.getUkupnoVrijeme() /1000 /60, (rezultat.getUkupnoVrijeme()/1000)%60) +
                    "\n--------------------------";
            rezultatTextView.setText(rezultatString);
        }
    }
}
