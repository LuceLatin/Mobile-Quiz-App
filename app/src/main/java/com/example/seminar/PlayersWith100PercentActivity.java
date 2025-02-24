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
import java.util.List;

public class PlayersWith100PercentActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private List<Rezultat> rezultatiList;
    private Top10Adapter rezultatiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_with_100_percent);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerViewPlayersWith100Percent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rezultatiList = new ArrayList<>();
        rezultatiAdapter = new Top10Adapter(rezultatiList);
        recyclerView.setAdapter(rezultatiAdapter);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String encodedEmail = encodeEmail(currentUser.getEmail());

            Query query = mDatabase.child("rezultati").orderByChild("brojTocnihOdgovora").equalTo(9);
            Toast.makeText(PlayersWith100PercentActivity.this, "100 posto."+ query, Toast.LENGTH_SHORT).show();


            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //rezultatiList.clear();
                    List<String> addedUsers = new ArrayList<>();

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userEmail = userSnapshot.getKey();

                        if (addedUsers.contains(userEmail)) {
                            continue;
                        }

                        String encodedEmail = decodeEmail(userEmail);
                        boolean userAdded = false;

                        for (DataSnapshot resultSnapshot : userSnapshot.getChildren()) {
                            Rezultat rezultat = resultSnapshot.getValue(Rezultat.class);

                            if (rezultat != null) {
                                rezultat.setEmail(encodedEmail);
                                rezultatiList.add(rezultat);
                                userAdded = true;
                                break;
                            }
                        }

                        if (userAdded) {
                            addedUsers.add(userEmail);
                        }
                    }

                    Collections.sort(rezultatiList, Comparator.comparingInt(Rezultat::getScore).reversed());

                    int brojRezultataZaPrikaz = Math.min(rezultatiList.size(), 10);
                    List<Rezultat> top10Rezultata = rezultatiList.subList(0, brojRezultataZaPrikaz);

                    if (rezultatiList.isEmpty()) {
                        Toast.makeText(PlayersWith100PercentActivity.this, "Nema dostupnih rezultata.", Toast.LENGTH_SHORT).show();
                    }

                    rezultatiAdapter.setRezultatiList(top10Rezultata);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    private String encodeEmail(String userEmail) {
        return Base64.encodeToString(userEmail.replace(".", ",").getBytes(), Base64.NO_WRAP);
    }

    private String decodeEmail(String encodedEmail) {
        byte[] data = Base64.decode(encodedEmail, Base64.NO_WRAP);
        return new String(data).replace(",", ".");
    }
}