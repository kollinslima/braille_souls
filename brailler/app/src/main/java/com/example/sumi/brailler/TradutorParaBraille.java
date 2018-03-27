package com.example.sumi.brailler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TradutorParaBraille extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradutor_para_braille);
        final ListView lv = (ListView) findViewById(R.id.tradutorListView);
        final Button btn = (Button) findViewById(R.id.buttonTraduzir);
        tv = (TextView) findViewById(R.id.tradutorCampoParaBraille);
        final List<String> braille_list = new ArrayList<String>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, braille_list);
        lv.setAdapter(arrayAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv = (TextView) findViewById(R.id.tradutorCampoParaBraille);
                String palavra = tv.getText().toString();
                //palavra.toUpperCase();
                braille_list.clear();
                for(int i = 0; i < palavra.length(); i++){
                    braille_list.add(MenuPrincipal.text_to_braille.get(String.valueOf(palavra.charAt(i))));
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }


}
