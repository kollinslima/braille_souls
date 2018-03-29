package com.example.sumi.brailler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toast;

import java.util.Map;

public class TradutorDoBraille extends AppCompatActivity {

    private ToggleButton[] tecladoBraile = new ToggleButton[6];
    private Switch numLock;
    private String campoDig, campoCarac;
    private TextView campoDigView, campoCaracView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradutor_do_braille);
        campoDigView = (TextView) findViewById(R.id.campoDigito);
        campoCaracView = (TextView) findViewById(R.id.campoCaractere);
        tecladoBraile[0] = (ToggleButton) findViewById(R.id.braille_button_1x1);
        tecladoBraile[1] = (ToggleButton) findViewById(R.id.braille_button_1x2);
        tecladoBraile[2] = (ToggleButton) findViewById(R.id.braille_button_2x1);
        tecladoBraile[3] = (ToggleButton) findViewById(R.id.braille_button_2x2);
        tecladoBraile[4] = (ToggleButton) findViewById(R.id.braille_button_3x1);
        tecladoBraile[5] = (ToggleButton) findViewById(R.id.braille_button_3x2);
    }

    public void onClickAdicionar(android.view.View view) {
        String resposta = "";

        for (ToggleButton button : tecladoBraile) {
            if (button.isChecked()) {
                resposta = resposta.concat("1");
            } else {
                resposta = resposta.concat("0");
            }
            button.setChecked(false);
        }
        if(MenuPrincipal.braille_to_text.get(resposta) == null){
            Toast.makeText(this, "Desconhecido", Toast.LENGTH_SHORT).show();
            return;
        }
        for(Map.Entry<String, String> it : MenuPrincipal.text_to_braille.entrySet()){
            if(it.getValue().equals(resposta)){
                if(Character.isDigit(it.getKey().charAt(0))){
                    campoDig = campoDigView.getText().toString();
                    campoDig = campoDig + it.getKey();
                    campoDigView.setText(campoDig);
                }else{
                    campoCarac = campoCaracView.getText().toString();
                    campoCarac = campoCarac + it.getKey();
                    campoCaracView.setText(campoCarac);
                }
            }
        }
    }

    public void onClickAdicionarEspaco(android.view.View view) {
        campoDig = campoDigView.getText().toString();
        campoDig = campoDig + " ";
        campoDigView.setText(campoDig);
        for (ToggleButton button : tecladoBraile) {
            button.setChecked(false);
        }
    }

    public void onClickVoltarTradutor(android.view.View view) {
        finish();
    }

}

