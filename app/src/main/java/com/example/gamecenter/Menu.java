package com.example.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

        setBtns();

    }

    private void setBtns() {
        play2048 = findViewById(R.id.play2048);
        play2048.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Joc2048.class);
                startActivityForResult(intent,1);
            }
        });

        playPegSolitaire = findViewById(R.id.playPegSolitaire);
        playPegSolitaire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, PegSolitaire.class);
                startActivityForResult(intent,2);
            }
        });

        playClose = findViewById(R.id.playClose);
        playClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    String user_name = data.getStringExtra("user_name");
                    String score = data.getStringExtra("score");
                    System.out.println("2048");
                    System.out.println("USERNAME ->" + user_name );
                    System.out.println("SCORE ->" + score );
                }
                break;
            }
            case (2) : {
                if (resultCode == Activity.RESULT_OK) {
                    String user_name = data.getStringExtra("user_name");
                    String score = data.getStringExtra("score");
                    System.out.println("PEG " );
                    System.out.println("USERNAME ->" + user_name );
                    System.out.println("SCORE ->" + score );
                }
                break;
            }
        }
    }
}