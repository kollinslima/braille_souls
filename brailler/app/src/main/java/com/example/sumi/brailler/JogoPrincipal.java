package com.example.sumi.brailler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class JogoPrincipal extends AppCompatActivity implements PauseFragment.onDismissListener {

    public static final String GAME_TAG_LOG = "gameLog";

    //10 segundos
    private final int TEMPO_CRONOMETRO = 10;
    private final int INTERVALO_TEMPO = 1000;   //ms

    private Timer temporizador;
    private long tempoRestante = TEMPO_CRONOMETRO;

    private String palavraSorteada;

    private TextView cronometro, palavra;

    private ToggleButton[] brailleKeyboard = new ToggleButton[6];

    private Button botaoPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo_principal);

        brailleKeyboard[0] = (ToggleButton) findViewById(R.id.braille_button_1x1);
        brailleKeyboard[1] = (ToggleButton) findViewById(R.id.braille_button_1x2);
        brailleKeyboard[2] = (ToggleButton) findViewById(R.id.braille_button_2x1);
        brailleKeyboard[3] = (ToggleButton) findViewById(R.id.braille_button_2x2);
        brailleKeyboard[4] = (ToggleButton) findViewById(R.id.braille_button_3x1);
        brailleKeyboard[5] = (ToggleButton) findViewById(R.id.braille_button_3x2);

        cronometro = (TextView) findViewById(R.id.cronometro);

        palavra = (TextView) findViewById(R.id.palavraProposta);

    }

    @Override
    protected void onResume() {
        super.onResume();
        reiniciaJogo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        temporizador.cancel();
    }

    private void reiniciaJogo() {
        palavraSorteada = sorteiaPalavra();
        palavra.setText(palavraSorteada);

        cronometro.setText(String.valueOf(TEMPO_CRONOMETRO));

        temporizador = new Timer();
        temporizador.scheduleAtFixedRate(new TempoJogo(), INTERVALO_TEMPO, INTERVALO_TEMPO);
    }

    private void verificaResposta() {
        String resposta = "";

        for (ToggleButton button : brailleKeyboard) {
            if (button.isChecked()) {
                resposta = resposta.concat("1");
            } else {
                resposta = resposta.concat("0");
            }

            button.setChecked(false);
        }

        try {
            if (MenuPrincipal.braille_to_text.get(resposta).equals(palavraSorteada)) {
                Toast.makeText(this, "Correto", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Errado", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, "Errado", Toast.LENGTH_SHORT).show();
        }

        temporizador.cancel();
        reiniciaJogo();
    }

    private String sorteiaPalavra() {
        Set<String> keys = MenuPrincipal.braille_to_text.keySet();
        int keyNumber = (int) (Math.random() * keys.size());
        Iterator<String> keyIterator = keys.iterator();

        String randomKey = null;

        for (int i = 0; i < keyNumber && keyIterator.hasNext(); i++) {
            randomKey = keyIterator.next();
        }

        if (randomKey != null) {
            return MenuPrincipal.braille_to_text.get(randomKey);
        } else {
            return null;
        }
    }

    public void onPauseClick(View view) {
        temporizador.cancel();
        new PauseFragment().abrir(getSupportFragmentManager());
    }

    @Override
    public void continuarJogo() {
        temporizador = new Timer();
        temporizador.scheduleAtFixedRate(new TempoJogo(), INTERVALO_TEMPO, INTERVALO_TEMPO);
    }

    @Override
    public void voltarMenuPrincipal() {
        temporizador.cancel();

        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
    }

    public void onCheckClick(View view) {
        verificaResposta();
    }

    public class TempoJogo extends TimerTask {

        @Override
        public void run() {

            tempoRestante -= 1;

            if (tempoRestante <= 0) {
                tempoRestante = TEMPO_CRONOMETRO;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        verificaResposta();
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cronometro.setText(String.valueOf(tempoRestante));
                    }
                });
            }
        }
    }

}
