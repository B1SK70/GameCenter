package com.example.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    Button play2048,playPegSolitaire,playClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        importBtns();

    }

    private void importBtns() {
        play2048 = findViewById(R.id.play2048);
        play2048.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Joc2048.class);
                startActivity(intent);
            }
        });

        playPegSolitaire = findViewById(R.id.playPegSolitaire);
        playPegSolitaire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, PegSolitaire.class);
                startActivity(intent);
            }
        });

        playClose = findViewById(R.id.playClose);
        playClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }
}