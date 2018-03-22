package com.example.sumi.brailler;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Iterator;
import java.util.Set;

public class JogoPrincipal extends AppCompatActivity {

    public static final String GAME_TAG_LOG = "gameLog";

    //10 segundos
    private final long TEMPO_CRONOMETRO = (long) (10 * Math.pow(10, 3));

    private TimerJogo timer;

    private String palavraSorteada;

    private TextView cronometro, palavra;

    private ToggleButton[] brailleKeyboard = new ToggleButton[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo_principal);

        timer = new TimerJogo(TEMPO_CRONOMETRO, 1000);

        brailleKeyboard[0] = (ToggleButton) findViewById(R.id.braille_button_1x1);
        brailleKeyboard[1] = (ToggleButton) findViewById(R.id.braille_button_1x2);
        brailleKeyboard[2] = (ToggleButton) findViewById(R.id.braille_button_2x1);
        brailleKeyboard[3] = (ToggleButton) findViewById(R.id.braille_button_2x2);
        brailleKeyboard[4] = (ToggleButton) findViewById(R.id.braille_button_3x1);
        brailleKeyboard[5] = (ToggleButton) findViewById(R.id.braille_button_3x2);

        cronometro = (TextView) findViewById(R.id.cronometro);
        cronometro.setText(TEMPO_CRONOMETRO / 1000 + " s");

        palavra = (TextView) findViewById(R.id.palavraProposta);

        reiniciaJogo();
    }

    private void reiniciaJogo() {
        palavraSorteada = sorteiaPalavra();
        palavra.setText(palavraSorteada);
        cronometro.setTextColor(Color.BLACK);
        timer.start();
    }

    private void verificaResposta(){
        String resposta = "";

        for (ToggleButton button : brailleKeyboard){
            if (button.isChecked()){
                resposta = resposta.concat("1");
            } else {
                resposta = resposta.concat("0");
            }

            button.setChecked(false);
        }

        try {
            if (MainActivity.braille_to_text.get(resposta).equals(palavraSorteada)) {
                Toast.makeText(this, "Correto", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Errado", Toast.LENGTH_SHORT).show();
            }
        }catch(NullPointerException e){
            Toast.makeText(this, "Errado", Toast.LENGTH_SHORT).show();
        }

        reiniciaJogo();
    }

    private String sorteiaPalavra() {
        Set<String> keys = MainActivity.braille_to_text.keySet();
        int keyNumber = (int) (Math.random() * keys.size());
        Iterator<String> keyIterator = keys.iterator();

        String randomKey = null;

        for (int i = 0; i < keyNumber && keyIterator.hasNext(); i++) {
            randomKey = keyIterator.next();
        }

        if (randomKey != null) {
            return MainActivity.braille_to_text.get(randomKey);
        } else {
            return null;
        }
    }

    public class TimerJogo extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimerJogo(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            cronometro.setText(millisUntilFinished / 1000 + " s");
        }

        @Override
        public void onFinish() {
            cronometro.setText(0 + " s");
            cronometro.setTextColor(Color.RED);
            verificaResposta();
        }
    }
}
