package com.example.sumi.brailler;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String DATABASE_TAG = "DatabaseTest";

    private DataBaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DataBaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        databaseTest();

    }

    public void onClickStartButton(View view) {
        Intent intent = new Intent(this, TecladoComTela.class);
        startActivity(intent);
    }

    public void onClickDictionatyButton(View view) {
        return;
    }

    public void onClickExitButton(View view) {
        finish();
    }

    private void databaseTest(){
        /*******************************Database Test***********************************/
        try {
            mDb = mDBHelper.getWritableDatabase();
//            Cursor c = mDb.query("brailleTable",null, "PlainText=?",new String[]{"A"},null,null,null);
            Cursor c = mDb.query("brailleTable",null, null,null,null,null,null);
//            Cursor  c = mDb.rawQuery("select * from brailleTable",null);

            if (c.moveToFirst()) {
                do {
                    Log.d(DATABASE_TAG, "Texto: " + c.getString(c.getColumnIndex("PlainText"))
                            + " - Braille: " + c.getString(c.getColumnIndex("Braille")));

                } while (c.moveToNext());
            }

        } catch (SQLException mSQLException) {
            throw mSQLException;
        } finally {
            mDb.close();
        }
        /*********************************************************************************/
    }
}
