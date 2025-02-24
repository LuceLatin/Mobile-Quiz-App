package com.example.seminar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PocetnaStranicaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna_stranica);

        Button buttonStart = findViewById(R.id.buttonStart);
        Button buttonResults = findViewById(R.id.buttonResults);
        Button obrana = findViewById(R.id.StoPosto);


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PocetnaStranicaActivity.this, IgraActivity.class));
            }
        });

        buttonResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PocetnaStranicaActivity.this, Top10Activity.class));
            }
        });

        obrana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PocetnaStranicaActivity.this, PlayersWith100PercentActivity.class));
            }
        });
    }
}
