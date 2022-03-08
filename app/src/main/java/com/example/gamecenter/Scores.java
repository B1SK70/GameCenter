package com.example.gamecenter;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Scores extends AppCompatActivity {

    SQLiteDatabase db;

    RecyclerView g2048, pegSolitaire;
    Button empty2048, emptyPeg;


    ArrayList<String[]> pegScores = new ArrayList<>();
    ArrayList<String[]> g2048Scores = new ArrayList<>();

    ScoresAdapter pegAdapter, g2048Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);
        wakeUpDB();

        bindXml();

        getScoresValues();
        createAdapters();
        bindAdapters();


        //showScores();

    }

    private void createAdapters() {
        g2048Adapter = new ScoresAdapter(g2048Scores);
        g2048.setAdapter(g2048Adapter);

        pegAdapter = new ScoresAdapter(pegScores);
        pegSolitaire.setAdapter(pegAdapter);
    }

    private void bindAdapters() {
        empty2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteScores(0);
            }
        });
        emptyPeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteScores(1);
            }
        });
    }

    private void getScoresValues() {

        ArrayList<String[]> g2048ScoresUnsorted = new ArrayList<>();
        Cursor g2048ScoresCURSOR = selectByTable("g2048");
        if (g2048ScoresCURSOR.moveToFirst()) do {
            String[] newItem = new String[2];
            newItem[0] = g2048ScoresCURSOR.getString(1);
            newItem[1] = g2048ScoresCURSOR.getString(2);
            g2048ScoresUnsorted.add(newItem);
        } while (g2048ScoresCURSOR.moveToNext());
        g2048ScoresCURSOR.close();

        ArrayList<String[]> pegScoresUnsorted = new ArrayList<>();
        Cursor pegScoresCURSOR = selectByTable("gpeg");
        if (pegScoresCURSOR.moveToFirst()) do {
            String[] newItem = new String[2];
            newItem[0] = pegScoresCURSOR.getString(1);
            newItem[1] = pegScoresCURSOR.getString(2);
            pegScoresUnsorted.add(newItem);
        } while (pegScoresCURSOR.moveToNext());
        pegScoresCURSOR.close();

        sortScores(g2048Scores, g2048ScoresUnsorted, "desc");
        sortScores(pegScores, pegScoresUnsorted, "asc");
    }

    private void sortScores(ArrayList<String[]> fill, ArrayList<String[]> unsorted, String sortTypeString) {



        int sortType = (sortTypeString == "desc") ? 0 : 1;

        int size = unsorted.size();
        for (int i = 0; i < size; i++) {
            String[] newValeToInsert = null;

            for (String[] iterating : unsorted) {
                if (newValeToInsert == null) newValeToInsert = iterating;

                if (sortType == 0) {
                    if (Integer.parseInt(iterating[1]) > Integer.parseInt(newValeToInsert[1]))
                        newValeToInsert = iterating;
                } else if (sortType == 1) {
                    if (Integer.parseInt(iterating[1]) < Integer.parseInt(newValeToInsert[1]))
                        newValeToInsert = iterating;
                }
            }
            fill.add(newValeToInsert);
            unsorted.remove(newValeToInsert);
        }

    }

    private void bindXml() {
        g2048 = (RecyclerView) findViewById(R.id.g2048);
        g2048.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        pegSolitaire = (RecyclerView) findViewById(R.id.pegSolitaire);
        pegSolitaire.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        empty2048 = (Button) findViewById(R.id.empty2048);
        emptyPeg = (Button) findViewById(R.id.emptyPeg);
    }

    private void deleteScores(int i) {
        //0 -> 2048     1 -> Peg
        clearTable(i);

        pegScores.clear();
        g2048Scores.clear();

        getScoresValues();

        g2048Adapter.notifyDataSetChanged();
        pegAdapter.notifyDataSetChanged();
    }

    private void wakeUpDB() {
        db = openOrCreateDatabase("MyDatabase", MODE_PRIVATE, null);
        try {
            db.execSQL("CREATE TABLE users (_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, password TEXT)");
            db.execSQL("CREATE TABLE g2048 (_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, score REAL)");
            db.execSQL("CREATE TABLE gpeg (_id INTEGER PRIMARY KEY AUTOINCREMENT, user_name TEXT, score REAL)");

            insertUserIntoDB("arnau", "arnau");
        } catch (Exception e) {
            System.out.println("table existent, skipping create table");
        }
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

    private void showScores() {
        Cursor scores = selectByTable("g2048");

        if (scores.moveToFirst()) do {

            System.out.println(scores.getString(1));
            System.out.println(scores.getString(2));
            System.out.println("---");

        } while (scores.moveToNext());
        scores.close();

    }

    private Cursor selectByTable(String desiredTitle) {
        return db.rawQuery("SELECT * FROM " + desiredTitle, null);
    }

    private void clearTable(int gameCode) {
        String tableName = (gameCode == 0) ? "g2048" : "gpeg";
        db.execSQL("delete from " + tableName);
    }
}
