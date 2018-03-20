package com.example.sumi.brailler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClickStartButton(View view) {
        return;
    }

    public void onClickDictionatyButton(View view) {
        return;
    }

    public void onClickExitButton(View view) {
        finish();
    }
}
