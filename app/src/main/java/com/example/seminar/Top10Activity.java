package com.example.seminar;

import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Top10Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private List<Rezultat> rezultatiList;
    private Top10Adapter rezultatiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerViewTop10);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rezultatiList = new ArrayList<>();
        rezultatiAdapter = new Top10Adapter(rezultatiList);
        recyclerView.setAdapter(rezultatiAdapter);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String encodedEmail = encodeEmail(currentUser.getEmail());

            Query query = mDatabase.child("rezultati").orderByChild("brojTocnihOdgovora").limitToLast(10);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    rezultatiList.clear();
                    Set<String> addedPlayers = new HashSet<>();

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String encodedEmail = decodeEmail(userSnapshot.getKey());
                        int gamesWith100Percent = countGamesWith100Percent(userSnapshot);

                        if (addedPlayers.add(encodedEmail)) {
                            for (DataSnapshot resultSnapshot : userSnapshot.getChildren()) {
                                Rezultat rezultat = resultSnapshot.getValue(Rezultat.class);

                                if (rezultat != null && rezultat.getScore() == 100) {
                                    rezultat.setEmail(encodedEmail);
                                    rezultat.setBrojOdigranihIgara(gamesWith100Percent);

                                    if (!containsUserWithEmail(rezultat.getName(), rezultatiList)) {
                                        rezultatiList.add(rezultat);
                                    }
                                }
                            }
                        }
                    }

                    // sortiranje rezultata prema postotku točnih odgovora i broju odigranih igara u silaznom redoslijedu
                    Collections.sort(rezultatiList, Comparator
                            .comparingInt(Rezultat::getScore)
                            .thenComparingInt(Rezultat::getBrojOdigranihIgara)
                            .reversed());
                    Toast.makeText(Top10Activity.this, "100 posto." + query, Toast.LENGTH_SHORT).show();

                    // top10
                    int brojRezultataZaPrikaz = Math.min(rezultatiList.size(), 10);
                    List<Rezultat> top10Rezultata = rezultatiList.subList(0, brojRezultataZaPrikaz);

                    if (rezultatiList.isEmpty()) {
                        Toast.makeText(Top10Activity.this, "Nema dostupnih rezultata.", Toast.LENGTH_SHORT).show();
                    }

                    rezultatiAdapter.setRezultatiList(top10Rezultata);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


// ...


// ...

        }
    }

    private boolean containsUserWithEmail(String email, List<Rezultat> rezultatiList) {
        for (Rezultat existingResult : rezultatiList) {
            if (existingResult.getName().equals(email)) {
                return true;
            }
        }
        return false;
    }

    private String encodeEmail(String userEmail) {
        return Base64.encodeToString(userEmail.replace(".", ",").getBytes(), Base64.NO_WRAP);
    }

    private String decodeEmail(String encodedEmail) {
        byte[] data = Base64.decode(encodedEmail, Base64.NO_WRAP);
        return new String(data).replace(",", ".");
    }

    // Dodano: Metoda za brojanje igara s 100% točnih odgovora
    private int countGamesWith100Percent(DataSnapshot userSnapshot) {
        int count = 0;
        for (DataSnapshot resultSnapshot : userSnapshot.getChildren()) {
            Rezultat rezultat = resultSnapshot.getValue(Rezultat.class);
            if (rezultat != null && rezultat.getScore() == 100) {
                count++;
            }
        }
        return count;
    }
}
