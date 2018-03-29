package com.example.sumi.brailler;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.HashMap;

public class MenuPrincipal extends AppCompatActivity {

    public static final String DATABASE_TAG = "DatabaseTest";
    public static final HashMap<String, String> text_to_braille = new HashMap<String, String>();
    public static final HashMap<String, String> braille_to_text = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);

        inicializaTradutor();

    }

    private void inicializaTradutor() {

        DataBaseHelper mDBHelper = new DataBaseHelper(this);
        SQLiteDatabase mDb = null;

        try {
            mDBHelper.updateDataBase();
            mDb = mDBHelper.getWritableDatabase();

            Cursor cursor = mDb.query("brailleTable",null, null,null,null,null,null);

            if (cursor.moveToFirst()) {
                do {

                    text_to_braille.put(cursor.getString(cursor.getColumnIndex("PlainText")),
                            cursor.getString(cursor.getColumnIndex("Braille")));

                    braille_to_text.put(cursor.getString(cursor.getColumnIndex("Braille")),
                            cursor.getString(cursor.getColumnIndex("PlainText")));

//                    Log.d(DATABASE_TAG, "Braille: " + cursor.getString(cursor.getColumnIndex("Braille"))
//                            + " - Text: " + braille_to_text.get(cursor.getString(cursor.getColumnIndex("Braille"))));
                } while (cursor.moveToNext());
            }

        } catch (SQLException mSQLException) {
            throw mSQLException;
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        } finally {
            mDb.close();
        }
    }

    public void onClickStartButton(View view) {
        Intent intent = new Intent(this, SeletorDificuldade.class);
        startActivity(intent);
    }

    public void onClickDictionatyButton(View view) {
        Intent intent = new Intent(this, TradutorParaBraille.class);
        startActivity(intent);
    }

    public void onClickExitButton(View view) {
        finish();
    }
}
