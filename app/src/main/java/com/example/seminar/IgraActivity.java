package com.example.seminar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IgraActivity extends AppCompatActivity {

    private TextView pitanjeTextView, timerTextView;
    private Button odgovorAButton, odgovorBButton, odgovorCButton, odgovorDButton;

    private static final String KEY_TRENUTNO_PITANJE = "trenutno_pitanje";
    private static final String KEY_REZULTAT = "rezultat";
    private static final String KEY_VRIJEME = "vrijeme";

    private int rezultat = 0;
    private DatabaseReference databaseReference;
    private int trenutnoPitanje = 0;
    private Pitanje trenutno;
    private List<Pitanje> svaPitanja = new ArrayList<>();
    private List<Integer> randomPitanjaIndexes = new ArrayList<>();
    private int brojTocnihOdgovora = 0;
    private CountDownTimer countDownTimer;
    private long preostaloVrijeme;

    private long ukupnoVrijeme = 2 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_igra);

        pitanjeTextView = findViewById(R.id.pitanjeTextView);
        odgovorAButton = findViewById(R.id.odgovorAButton);
        odgovorBButton = findViewById(R.id.odgovorBButton);
        odgovorCButton = findViewById(R.id.odgovorCButton);
        odgovorDButton = findViewById(R.id.odgovorDButton);

        timerTextView = findViewById(R.id.timerTextView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("pitanja");

        dohvatiUkupanBrojPitanja();

        dodajOnClickListenerZaOdgovore();

        preostaloVrijeme = ukupnoVrijeme;
        startCountdownTimer(preostaloVrijeme);
    }



    private void dohvatiUkupanBrojPitanja() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Pitanje pitanje = snapshot.getValue(Pitanje.class);
                        if (pitanje != null) {
                            svaPitanja.add(pitanje);
                        }
                    }
                    generirajRandomPitanja();
                    prikaziSljedecePitanje();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void generirajRandomPitanja() {
        Collections.shuffle(svaPitanja);
        randomPitanjaIndexes.clear();

        for (int i = 0; i < 10; i++) {
            randomPitanjaIndexes.add(i);
        }
    }

    private void prikaziSljedecePitanje() {
        if (trenutnoPitanje < 10) {
            int randomIndex = randomPitanjaIndexes.get(trenutnoPitanje);

            trenutno = svaPitanja.get(randomIndex);
            postaviPitanje(trenutno);

            trenutnoPitanje++;
        } else {
            prikaziRezultat();
        }
    }

    private void postaviPitanje(Pitanje pitanje) {
        pitanjeTextView.setText(pitanje.getPitanje());
        odgovorAButton.setText(pitanje.getOdgovori().get("a"));
        odgovorBButton.setText(pitanje.getOdgovori().get("b"));
        odgovorCButton.setText(pitanje.getOdgovori().get("c"));
        odgovorDButton.setText(pitanje.getOdgovori().get("d"));
    }

    private void odaberiOdgovor(String odabraniOdgovor) {
        boolean jeLiOdgovorTocan = odabraniOdgovor.equals(trenutno.getTocanOdgovor());

        if (jeLiOdgovorTocan) {
            brojTocnihOdgovora++;
            // Povećaj vrijeme za 10 sekundi
            //preostaloVrijeme += 10 * 1000;
            //updateTimerText();

        }
        else{
            // Oduzimanje 10 sekundi od preostalog vremena
            countDownTimer.cancel(); // Prekini trenutni timer
            countDownTimer = new CountDownTimer(preostaloVrijeme - 10 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    preostaloVrijeme = millisUntilFinished;
                    updateTimerText();
                }

                @Override
                public void onFinish() {
                    prikaziRezultat();
                }
            }.start();

            updateTimerText();
            }



        String poruka = jeLiOdgovorTocan ? "Točan odgovor!" : "Netočan odgovor!";
        Toast.makeText(this, poruka, Toast.LENGTH_SHORT).show();

        prikaziSljedecePitanje();

    }

    private void prikaziRezultat() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        Intent intent = new Intent(this, RezultatActivity.class);
        intent.putExtra("BROJ_TOCNIH_ODGOVORA", brojTocnihOdgovora);
        intent.putExtra("UKUPNO_PITANJA", 10);
        intent.putExtra("PREOSTALO-VRIJEME", preostaloVrijeme);
        intent.putExtra("UKUPNO-VRIJEME", ukupnoVrijeme);


        startActivity(intent);

        finish();
    }

    private void dodajOnClickListenerZaOdgovore() {
        odgovorAButton.setOnClickListener(view -> odaberiOdgovor("a"));
        odgovorBButton.setOnClickListener(view -> odaberiOdgovor("b"));
        odgovorCButton.setOnClickListener(view -> odaberiOdgovor("c"));
        odgovorDButton.setOnClickListener(view -> odaberiOdgovor("d"));
    }

    private void startCountdownTimer(long vremenskiInterval) {
        countDownTimer = new CountDownTimer(vremenskiInterval, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                preostaloVrijeme = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                prikaziRezultat();
            }
        }.start();
    }

    private void updateTimerText() {
        long minute = preostaloVrijeme / 1000 / 60;
        long sekunde = (preostaloVrijeme / 1000) % 60;
        String vrijemeString = String.format("%02d:%02d", minute, sekunde);
        timerTextView.setText(vrijemeString);
    }
}
