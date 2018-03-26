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

    public static final String SELETOR_DIFICULDADE = "seletor_dificuldade";
    public static final String DIFICULDADE_PROGRESSIVA = "dificuldade_progressiva";

    public static final String FACIL = "facil";
    public static final String MEDIO = "medio";
    public static final String DIFICIL = "dificil";

    private final int ACERTOS_TROCA_NIVEL = 10;
    private final int ERROS_TROCA_NIVEL = 5;

    public static final String GAME_TAG_LOG = "gameLog";

    //10 segundos
    private final int TEMPO_CRONOMETRO = 10;
    private final int INTERVALO_TEMPO = 1000;   //ms

    private Timer temporizador;
    private long tempoRestante = TEMPO_CRONOMETRO;

    private String palavraSorteada;

    private TextView cronometro, palavra;

    private ToggleButton[] tecladoBraile = new ToggleButton[6];

    private Button botaoPause;

    private boolean ativaResposta, ativaTempo, dificuldadeProgressiva;

    private TextView txtAcertos, txtErros;
    int acertos, erros, acertosConsecutivos, errosConsecutivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo_principal);

        tecladoBraile[0] = (ToggleButton) findViewById(R.id.braille_button_1x1);
        tecladoBraile[1] = (ToggleButton) findViewById(R.id.braille_button_1x2);
        tecladoBraile[2] = (ToggleButton) findViewById(R.id.braille_button_2x1);
        tecladoBraile[3] = (ToggleButton) findViewById(R.id.braille_button_2x2);
        tecladoBraile[4] = (ToggleButton) findViewById(R.id.braille_button_3x1);
        tecladoBraile[5] = (ToggleButton) findViewById(R.id.braille_button_3x2);

        errosConsecutivos = 0;
        acertosConsecutivos = 0;
        acertos = 0;
        erros = 0;

        txtAcertos = (TextView) findViewById(R.id.pontos_acertos);
        txtAcertos.setText(String.valueOf(acertos));
        txtErros = (TextView) findViewById(R.id.pontos_erros);
        txtErros.setText(String.valueOf(erros));

        cronometro = (TextView) findViewById(R.id.cronometro);

        palavra = (TextView) findViewById(R.id.palavraProposta);

        String dificuldade = getIntent().getExtras().getString(SELETOR_DIFICULDADE);

        if (dificuldade.equals(FACIL)) {
            ativaTempo = false;
            ativaResposta = true;
        } else if (dificuldade.equals(MEDIO)) {
            ativaTempo = false;
            ativaResposta = false;
        } else {
            ativaTempo = true;
            ativaResposta = false;
        }

        dificuldadeProgressiva = getIntent().getExtras().getBoolean(DIFICULDADE_PROGRESSIVA);

    }

    @Override
    protected void onResume() {
        super.onResume();
        reiniciaJogo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ativaTempo) {
            temporizador.cancel();
        }
    }

    private void reiniciaJogo() {
        if (dificuldadeProgressiva) {
            trocaNivel();
        }

        palavraSorteada = sorteiaPalavra();
        palavra.setText(palavraSorteada);

        if (ativaResposta) {
            marcaResposta();
        }

        if (ativaTempo) {
            cronometro.setText(String.valueOf(TEMPO_CRONOMETRO));

            tempoRestante = TEMPO_CRONOMETRO;
            temporizador = new Timer();
            temporizador.scheduleAtFixedRate(new TempoJogo(), INTERVALO_TEMPO, INTERVALO_TEMPO);
        }
    }

    private void trocaNivel(){
        //Aumenta dificuldade
        if (acertosConsecutivos >= ACERTOS_TROCA_NIVEL) {
            if (ativaResposta) {
                Toast.makeText(this, "Dificuldade média", Toast.LENGTH_SHORT).show();
                acertosConsecutivos = 0;
                errosConsecutivos = 0;
                ativaResposta = false;

                for (ToggleButton button : tecladoBraile) {
                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.teclado_braile_normal));
                }

            } else {
                Toast.makeText(this, "Dificuldade difícil", Toast.LENGTH_SHORT).show();
                acertosConsecutivos = 0;
                errosConsecutivos = 0;
                ativaTempo = true;
            }
        }
        //Diminui dificuldade
        else if (errosConsecutivos >= ERROS_TROCA_NIVEL){
            if (ativaTempo){
                Toast.makeText(this, "Dificuldade média", Toast.LENGTH_SHORT).show();
                acertosConsecutivos = 0;
                errosConsecutivos = 0;

                cronometro.setText("");
                ativaTempo = false;
            } else {
                Toast.makeText(this, "Dificuldade fácil", Toast.LENGTH_SHORT).show();
                acertosConsecutivos = 0;
                errosConsecutivos = 0;
                ativaResposta = true;
            }
        }
    }

    private void marcaResposta() {
        String palavraBraile = MenuPrincipal.text_to_braille.get(palavraSorteada);
        int index = 0;

        for (ToggleButton button : tecladoBraile) {
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.teclado_braile_normal));

            if (palavraBraile.charAt(index) == '1') {
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.teclado_braile_com_dica));
            }
            index += 1;
        }
    }

    private void verificaResposta() {
        String resposta = "";

        for (ToggleButton button : tecladoBraile) {
            if (button.isChecked()) {
                resposta = resposta.concat("1");
            } else {
                resposta = resposta.concat("0");
            }

            button.setChecked(false);
        }

        try {
            if (MenuPrincipal.braille_to_text.get(resposta).equals(palavraSorteada)) {
                acertos += 1;
                acertosConsecutivos += 1;
                errosConsecutivos = 0;
                txtAcertos.setText(String.valueOf(acertos));
            } else {
                acertosConsecutivos = 0;
                errosConsecutivos += 1;
                erros += 1;
                txtErros.setText(String.valueOf(erros));
            }
        } catch (NullPointerException e) {
            acertosConsecutivos = 0;
            erros += 1;
            errosConsecutivos += 1;
            txtErros.setText(String.valueOf(erros));
        }

        if (ativaTempo) {
            temporizador.cancel();
        }
        reiniciaJogo();
    }

    private String sorteiaPalavra() {
        String randomKey = null;
        do {
            Set<String> keys = MenuPrincipal.braille_to_text.keySet();
            int keyNumber = (int) (Math.random() * keys.size());
            Iterator<String> keyIterator = keys.iterator();

            for (int i = 0; i < keyNumber && keyIterator.hasNext(); i++) {
                randomKey = keyIterator.next();
            }

        } while (randomKey == null);

        return MenuPrincipal.braille_to_text.get(randomKey);
    }

    public void onPauseClick(View view) {
        if (ativaTempo) {
            temporizador.cancel();
        }
        new PauseFragment().abrir(getSupportFragmentManager());
    }

    @Override
    public void continuarJogo() {
        if (ativaTempo) {
            temporizador = new Timer();
            temporizador.scheduleAtFixedRate(new TempoJogo(), INTERVALO_TEMPO, INTERVALO_TEMPO);
        }
    }

    @Override
    public void voltarMenuPrincipal() {
        if (ativaTempo) {
            temporizador.cancel();
        }

        finish();
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
