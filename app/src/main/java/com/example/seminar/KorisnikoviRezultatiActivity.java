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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class KorisnikoviRezultatiActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String userEmail;

    private RecyclerView rezultatiRecyclerView;
    private KorisnikoviRezultatiAdapter rezultatiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korisnikovi_rezultati);
        Toast.makeText(this, "Korisnikovi Rezultati.", Toast.LENGTH_SHORT).show();


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
        }

        rezultatiRecyclerView = findViewById(R.id.rezultatiRecyclerView);
        rezultatiRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        rezultatiAdapter = new KorisnikoviRezultatiAdapter();
        rezultatiRecyclerView.setAdapter(rezultatiAdapter);

        prikaziRezultate();
    }

    private void prikaziRezultate() {
        String encodedEmail = encodeEmail(userEmail);


        DatabaseReference rezultatiReference = mDatabase.child("rezultati").child(encodedEmail);

        rezultatiReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Rezultat> rezultatiList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Rezultat rezultat = snapshot.getValue(Rezultat.class);
                    if (rezultat != null) {
                        rezultatiList.add(rezultat);
                    }
                }
                Toast.makeText(KorisnikoviRezultatiActivity.this, "Ukupan broj rezultata: " + rezultatiList.size(), Toast.LENGTH_SHORT).show();

                rezultatiAdapter.setRezultatiList(rezultatiList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(KorisnikoviRezultatiActivity.this, "Greškaa rezultata: " , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String encodeEmail(String userEmail) {
        return Base64.encodeToString(userEmail.replace(".", ",").getBytes(), Base64.NO_WRAP);
    }

}
