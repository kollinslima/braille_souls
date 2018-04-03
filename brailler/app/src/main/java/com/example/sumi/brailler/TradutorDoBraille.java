package com.example.sumi.brailler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toast;

public class TradutorDoBraille extends AppCompatActivity {

    private ToggleButton[] tecladoBraile = new ToggleButton[6];
    private Switch numLock;
    private String campoRes;
    private TextView campoResView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradutor_do_braille);
        campoResView = (TextView) findViewById(R.id.campoResultado);
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
        campoRes = campoResView.getText().toString();
        resposta = MainMenu.braille_to_text.get(resposta).toString();
        if(resposta.equals("[]")){
            Toast.makeText(this, "Desconhecido", Toast.LENGTH_SHORT).show();
            return;
        }
        if(resposta.length() > 3){
            resposta = Character.toString(resposta.charAt(1)) + "/" + Character.toString(resposta.charAt(4));
            campoRes = campoRes + resposta;
        }else{
            resposta = Character.toString(resposta.charAt(1));
            campoRes = campoRes + resposta;
        }
        campoResView.setText(campoRes);
    }

    public void onClickAdicionarEspaco(android.view.View view) {
        campoRes = campoResView.getText().toString();
        campoRes = campoRes + " ";
        campoResView.setText(campoRes);
        for (ToggleButton button : tecladoBraile) {
            button.setChecked(false);
        }
    }

    public void onClickVoltarTradutor(android.view.View view) {
        finish();
    }

}

