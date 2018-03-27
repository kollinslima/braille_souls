package com.example.sumi.brailler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toast;

public class TradutorDoBraille extends AppCompatActivity {

    private ToggleButton[] tecladoBraile = new ToggleButton[6];
    private Switch numLock;
    private String resultadoCampo;
    private TextView resultadoCampoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradutor_do_braille);
        resultadoCampoView = (TextView) findViewById(R.id.resultadoCampo);
        tecladoBraile[0] = (ToggleButton) findViewById(R.id.braille_button_1x1);
        tecladoBraile[1] = (ToggleButton) findViewById(R.id.braille_button_1x2);
        tecladoBraile[2] = (ToggleButton) findViewById(R.id.braille_button_2x1);
        tecladoBraile[3] = (ToggleButton) findViewById(R.id.braille_button_2x2);
        tecladoBraile[4] = (ToggleButton) findViewById(R.id.braille_button_3x1);
        tecladoBraile[5] = (ToggleButton) findViewById(R.id.braille_button_3x2);
        numLock = (Switch) findViewById(R.id.button_numLock);
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
        resultadoCampo = resultadoCampoView.getText().toString();
        resultadoCampo = resultadoCampo + MenuPrincipal.braille_to_text.get(resposta);
        resultadoCampoView.setText(resultadoCampo);
    }

    public void onClickAdicionarEspaco(android.view.View view) {
        resultadoCampo = resultadoCampoView.getText().toString();
        resultadoCampo = resultadoCampo + " ";
        resultadoCampoView.setText(resultadoCampo);
        for (ToggleButton button : tecladoBraile) {
            button.setChecked(false);
        }
    }

    public void onClickVoltarTradutor(android.view.View view) {
        finish();
    }
}

