package com.example.seminar;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;

public class RezultatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private int brojTocnihOdgovora;
    private int ukupnoPitanja;
    private long preostaloVrijeme;
    private long ukupnoVrijeme;

    private int postotakTocnih;

    private String userEmail;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezultat);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
        }

        brojTocnihOdgovora = getIntent().getIntExtra("BROJ_TOCNIH_ODGOVORA", 0);
        ukupnoPitanja = getIntent().getIntExtra("UKUPNO_PITANJA", 0);
        preostaloVrijeme = getIntent().getLongExtra("PREOSTALO-VRIJEME", 0);
        ukupnoVrijeme = getIntent().getLongExtra("UKUPNO-VRIJEME", 0);

        Button spremiRezultatButton = findViewById(R.id.spremiRezultatButton);
        Button igrajPonovnoButton = findViewById(R.id.igrajPonovnoButton);
        Button pogledajRezultateButton = findViewById(R.id.pogledajRezultateButton);


        TextView rezultatTextView = findViewById(R.id.rezultatTextView);

        int postotakTocnih = (int) ((float) brojTocnihOdgovora / ukupnoPitanja * 100);
        long preostaleMinute = preostaloVrijeme / 1000 / 60;
        long preostaleSekunde = (preostaloVrijeme / 1000) % 60;
        long ukupneMinute = ukupnoVrijeme / 1000 / 60;
        long ukupneSekunde = (ukupnoVrijeme / 1000) % 60;

        String rezultat = "Broj točnih odgovora: " + brojTocnihOdgovora + "/" + ukupnoPitanja +
                "\nPostotak točnosti: " + postotakTocnih + "%" +
                "\nPreostalo vrijeme: " + String.format("%02d:%02d", preostaleMinute, preostaleSekunde) +
                "/" + String.format("%02d:%02d", ukupneMinute, ukupneSekunde);
        rezultatTextView.setText(rezultat);

        if (postotakTocnih >= 10) {
            showCongratulations();
        }

        spremiRezultatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spremiRezultatUBazu(userEmail);
            }
        });

        igrajPonovnoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                igrajPonovno();
            }
        });
        pogledajRezultateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pogledajRezultate();
            }
        });
    }

    private void showCongratulations() {
        LottieAnimationView lottieAnimationView = findViewById(R.id.lottieAnimationView);

        lottieAnimationView.setAnimation("animation.json");

        lottieAnimationView.playAnimation();

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Toast.makeText(RezultatActivity.this, "BRAVOO!!.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void spremiRezultatUBazu(String userEmail) {
        String encodedEmail = encodeEmail(userEmail);
        String rezultatiKey = mDatabase.child("rezultati").child(encodedEmail).push().getKey();

        Rezultat rezultat = new Rezultat(brojTocnihOdgovora, ukupnoPitanja, postotakTocnih, preostaloVrijeme, ukupnoVrijeme);

        mDatabase.child("rezultati").child(encodedEmail).child(rezultatiKey).setValue(rezultat)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RezultatActivity.this, "Rezultat uspješno spremljen u bazu!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RezultatActivity.this, "Greška prilikom spremanja rezultata.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String encodeEmail(String userEmail) {
        return Base64.encodeToString(userEmail.replace(".", ",").getBytes(), Base64.NO_WRAP);
    }

    private void igrajPonovno() {
        Intent intent = new Intent(this, PocetnaStranicaActivity.class);
        startActivity(intent);
        finish();
    }

    private void pogledajRezultate() {
        Intent intent = new Intent(this, KorisnikoviRezultatiActivity.class);
        startActivity(intent);
    }
}
