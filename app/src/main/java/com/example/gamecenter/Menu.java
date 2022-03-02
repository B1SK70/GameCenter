package com.example.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    SQLiteDatabase db;

    Button play2048, playPegSolitaire, playScore, playClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        wakeUpDB();


        setBtns();

    }

    private void setBtns() {
        play2048 = findViewById(R.id.play2048);
        play2048.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Joc2048.class);
                startActivityForResult(intent, 1);
            }
        });

        playPegSolitaire = findViewById(R.id.playPegSolitaire);
        playPegSolitaire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, PegSolitaire.class);
                startActivityForResult(intent, 2);
            }
        });

        playScore = findViewById(R.id.playScore);
        playScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Scores.class);
                startActivityForResult(intent, 2);
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

        if (resultCode == Activity.RESULT_OK) {
            String user_name = data.getStringExtra("user_name");
            String score = data.getStringExtra("score");

            saveScore(requestCode, user_name, Integer.parseInt(score));
        }
    }

    private void saveScore(int gameCode, String user_name, int score) {
        // 1 -> 2048         2 -> PegSolitaire
        String gameName = ( gameCode == 1 ) ? "g2048" : "gpeg";
        System.out.println(gameName + " -> '" + user_name + "' --> " + score );
        insertIntoDB(gameName, user_name, score);
    }

    private void wakeUpDB() {
        db = openOrCreateDatabase("MyDatabase", MODE_PRIVATE, null);
        try {
            db.execSQL("CREATE TABLE g2048 (_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, score REAL)");
            db.execSQL("CREATE TABLE gpeg (_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, core REAL)");
        } catch (Exception e) {
            System.out.println("table existent, skipping create table");
        }
    }

    private void insertIntoDB(String game, String user_name, int score) {
        ContentValues values = new ContentValues(2);
        values.put("user_name", user_name);
        values.put("score", score);
        db.insert(
                game,
                "_id",
                values
        );
    }

    private void deleteFromDB(int gameCode, int id) {
        String gameName = ( gameCode == 1 ) ? "g2048" : "gpeg";
        db.delete( gameName,
                "_id=?",
                new String[]{ String.valueOf(id) }
        );
    }

    private Cursor selectFromDB() {
        return db.rawQuery("SELECT _id, title, text FROM coments", null);
    }

    private Cursor selectByTitle(String desiredTitle) {
        return db.rawQuery("SELECT * FROM coments WHERE title =?", new String[]{desiredTitle});
    }

    private void clearTable( int gameCode ) {
        db.execSQL("delete from coments");
    }
}