package com.example.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    SharedPreferences sharedPref;
    SQLiteDatabase db;

    Button play2048, playPegSolitaire, playScore, playClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sharedPref = getSharedPreferences("mypref", 0);
        wakeUpDB();

        checkUser();

        setBtns();

    }

//    private void laslalala() {
//        db.execSQL("CREATE TABLE users (_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, password TEXT)");
//        insertUserIntoDB("arnau", "arnau");
//    }

    private void checkUser() {
        if (sharedPref.getInt("userState", 0) == 0  ) {
            sendToLogin();
            finish();
        }
    }

    private void sendToLogin() {
        Intent intent = new Intent(Menu.this, Login.class);
        startActivity(intent);

    }

    private void setUserState(int state) {
        //  0 -> Not logged    1 -> Logged
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("userState", state);
        editor.commit();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 || requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String user_name = data.getStringExtra("user_name");
                String score = data.getStringExtra("score");

                saveScore(requestCode, user_name, Integer.parseInt(score));
            }
        } else {





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
            db.execSQL("CREATE TABLE users (_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, password TEXT)");
            db.execSQL("CREATE TABLE g2048 (_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, score REAL)");
            db.execSQL("CREATE TABLE gpeg (_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, core REAL)");

            insertUserIntoDB("arnau", "arnau");
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

    private void insertUserIntoDB(String user_name, String password) {
        ContentValues values = new ContentValues(2);
        values.put("user_name", user_name);
        values.put("password", password);
        db.insert(
                "users",
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