package com.example.gamecenter;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    SharedPreferences sharedPref;
    SQLiteDatabase db;

    EditText user_nameInput;
    EditText passwordInput;

    TextView warning;

    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences("mypref", 0);
        setContentView(R.layout.register);
        wakeUpDB();

        bindXMl();


    }

    private void bindXMl() {
        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserAvailavility();
            }
        });

        user_nameInput = (EditText) findViewById(R.id.user_nameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);

        warning = (TextView) findViewById(R.id.warning);

    }

    private void checkUserAvailavility() {
        boolean userExistent = false;
        Cursor response = db.rawQuery("SELECT user_name, password FROM users", null);

        if (response != null && response.moveToFirst()) {
            do {
                String user_name = response.getString(0);

                if (String.valueOf(user_nameInput.getText()).equals(user_name)) {
                    userExistent = true;
                    warnUser("User already exists");
                } else if (user_nameInput.getText().length() > 11 ) {
                    userExistent = true;
                    warnUser("User has to be 11 characters or less");
                }

            } while (response.moveToNext());
            response.close();

            if (!userExistent) createUser(
                    String.valueOf(user_nameInput.getText()),
                    String.valueOf(passwordInput.getText()) );
        }
    }

    private void warnUser(String waringText) {
        warning.setText(waringText);
    }

    private void createUser(String user_name, String password) {
        setUserState(1, user_name);

        insertUserIntoDB(user_name, password);
        sendToMenu();
    }


    private void sendToMenu() {
        Intent intent = new Intent(Register.this, Menu.class);
        startActivity(intent);

        finish();
    }

    private void setUserState(int state, String user_name) {
        //  0 -> Not logged    1 -> Logged
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("userState", state);
        editor.putString("user_name", user_name);

        editor.commit();
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
}
