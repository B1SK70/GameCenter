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

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    SharedPreferences sharedPref;
    SQLiteDatabase db;

    EditText user_nameInput;
    EditText passwordInput;

    Button login;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getSharedPreferences("mypref", 0);
        setContentView(R.layout.login);
        wakeUpDB();

        bindXMl();

    }

    private void bindXMl() {
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginCredentials();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToRegister();
            }
        });

        user_nameInput = (EditText) findViewById(R.id.user_nameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
    }

    private void sendToRegister() {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

    private void checkLoginCredentials() {
        Cursor response = db.rawQuery("SELECT user_name, password FROM users", null);
        if (response != null && response.moveToFirst()) {
            do {
                String user_name = response.getString(0);
                String password = response.getString(1);

                if (String.valueOf(user_nameInput.getText()).equals(user_name)
                        && String.valueOf(passwordInput.getText()).equals(password)) {

                    loginSuccesfull(user_name);
                }
            } while (response.moveToNext());
            response.close();
        }
    }

    private void loginSuccesfull(String user_name) {
        setUserState(1, user_name);

        Intent intent = new Intent(Login.this, Menu.class);
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
            System.out.println("TABLES CREATED AGAIN");

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
