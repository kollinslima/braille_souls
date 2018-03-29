package com.example.sumi.brailler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sumi.brailler.fragments.FimJogoFragment;
import com.example.sumi.brailler.fragments.PauseFragment;
import com.plusquare.clockview.ClockView;

import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class JogoPrincipal extends AppCompatActivity implements PauseFragment.onDismissListener, FimJogoFragment.onDismissListener {

    public static final String SELETOR_DIFICULDADE = "seletor_dificuldade";
    public static final String DIFICULDADE_PROGRESSIVA = "dificuldade_progressiva";

    public static final String FACIL = "facil";
    public static final String MEDIO = "medio";
    public static final String DIFICIL = "dificil";

    private final int ACERTOS_TROCA_NIVEL = 10;
    private final int ERROS_TROCA_NIVEL = 3;

    public static final String GAME_TAG_LOG = "gameLog";

    //10 segundos
    private final double TEMPO_CRONOMETRO = 10;
    private final int INTERVALO_TEMPO = (int) ((TEMPO_CRONOMETRO/60)*1000);   //ms

    private Timer temporizador;
//    private long tempoRestante = TEMPO_CRONOMETRO;
    private int tempoGrafico = 0;

    private String palavraSorteada;

    private TextView palavra;
    private ClockView cronometroGrafico;

    private ToggleButton[] tecladoBraile = new ToggleButton[6];

    private ImageView[] vidas = new ImageView[6];
    private boolean[] vidasControle = new boolean[6];

    private Button botaoPause;

    public static String dificuldadeJogo;
    public static boolean dificuldadeProgressiva;
    private boolean ativaResposta, ativaTempo;

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

        vidas[0] = (ImageView) findViewById(R.id.vida1);
        vidas[1] = (ImageView) findViewById(R.id.vida2);
        vidas[2] = (ImageView) findViewById(R.id.vida3);
        vidas[3] = (ImageView) findViewById(R.id.vida4);
        vidas[4] = (ImageView) findViewById(R.id.vida5);
        vidas[5] = (ImageView) findViewById(R.id.vida6);

        for (int i = 0; i < vidas.length; i++) {
            vidas[i].setImageResource(R.drawable.ic_vida_ativa);
            vidasControle[i] = true;
        }

        errosConsecutivos = 0;
        acertosConsecutivos = 0;
        acertos = 0;
        erros = 0;

        txtAcertos = (TextView) findViewById(R.id.pontos_acertos);
        txtAcertos.setText(String.valueOf(acertos));
        txtErros = (TextView) findViewById(R.id.pontos_erros);
        txtErros.setText(String.valueOf(erros));

//        cronometro = (TextView) findViewById(R.id.cronometro);
        cronometroGrafico = (ClockView) findViewById(R.id.cronometroGrafico);

        palavra = (TextView) findViewById(R.id.palavraProposta);

        dificuldadeJogo = getIntent().getExtras().getString(SELETOR_DIFICULDADE);

        if (dificuldadeJogo.equals(FACIL)) {
            ativaTempo = false;
            ativaResposta = true;
            cronometroGrafico.setVisibility(View.INVISIBLE);
        } else if (dificuldadeJogo.equals(MEDIO)) {
            ativaTempo = false;
            ativaResposta = false;
            cronometroGrafico.setVisibility(View.INVISIBLE);
        } else {
            ativaTempo = true;
            ativaResposta = false;
            cronometroGrafico.setVisibility(View.VISIBLE);
        }

        dificuldadeProgressiva = getIntent().getExtras().getBoolean(DIFICULDADE_PROGRESSIVA);

        novaPalavra();

    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseJogo();
    }

    private void novaPalavra() {
        if (dificuldadeProgressiva) {
            trocaNivel();
        }

        palavraSorteada = sorteiaPalavra();
        palavra.setText(palavraSorteada);

        if (ativaResposta) {
            marcaResposta();
        }
        continuarJogo();
    }

    private void trocaNivel() {
        //Aumenta dificuldade
        if (acertosConsecutivos >= ACERTOS_TROCA_NIVEL) {
            if (ativaResposta) {
                Toast.makeText(this, "Médio", Toast.LENGTH_SHORT).show();
                acertosConsecutivos = 0;
                errosConsecutivos = 0;
                ativaResposta = false;

                for (ToggleButton button : tecladoBraile) {
                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.teclado_braile_normal));
                }

            } else {
                if (!ativaTempo) {
                    Toast.makeText(this, "Difícil", Toast.LENGTH_SHORT).show();
                    acertosConsecutivos = 0;
                    errosConsecutivos = 0;
                    ativaTempo = true;
                }
            }
        }
        //Diminui dificuldade
        else if (errosConsecutivos >= ERROS_TROCA_NIVEL) {
            if (ativaTempo) {
                Toast.makeText(this, "Médio", Toast.LENGTH_SHORT).show();
                acertosConsecutivos = 0;
                errosConsecutivos = 0;

//                cronometro.setText("");
                ativaTempo = false;
            } else {
                if (!ativaResposta) {
                    Toast.makeText(this, "Fácil", Toast.LENGTH_SHORT).show();
                    acertosConsecutivos = 0;
                    errosConsecutivos = 0;
                    ativaResposta = true;
                }
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

        if (atualizaVidas()) {
            novaPalavra();
        } else {
            fimDeJogo();
        }
    }

    private boolean atualizaVidas() {
        if (errosConsecutivos == 0) {
            if (acertosConsecutivos >= ACERTOS_TROCA_NIVEL) {
                for (int i = 0; i < vidas.length; i++) {
                    if (!vidasControle[i]) {
                        vidas[i].setImageResource(R.drawable.ic_vida_ativa);
                        vidasControle[i] = true;
                        break;
                    }
                }

                if (!dificuldadeProgressiva) {
                    acertosConsecutivos = 0;
                }
            }
        } else {
            for (int i = vidas.length - 1; i >= 0; i--) {
                if (vidasControle[i]) {
                    vidas[i].setImageResource(R.drawable.ic_vida_perdida);
                    vidasControle[i] = false;
                    break;
                }
            }
        }

        return vidasControle[0];
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
        pauseJogo();
    }

    private void pauseJogo() {
        if (ativaTempo) {
            temporizador.cancel();
        }
        new PauseFragment().abrir(getSupportFragmentManager());
    }

    private void continuarJogo(){
        if (ativaTempo) {
            temporizador = new Timer();
            temporizador.scheduleAtFixedRate(new TempoJogo(), INTERVALO_TEMPO, INTERVALO_TEMPO);
        }
    }

    @Override
    public void continuarJogoFragment() {

        if (dificuldadeJogo.equals(FACIL)) {
            ativaTempo = false;
            ativaResposta = true;
            cronometroGrafico.setVisibility(View.INVISIBLE);
        } else if (dificuldadeJogo.equals(MEDIO)) {
            ativaTempo = false;
            ativaResposta = false;
            cronometroGrafico.setVisibility(View.INVISIBLE);

            for (ToggleButton button : tecladoBraile) {
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.teclado_braile_normal));
            }
        } else {
            ativaTempo = true;
            ativaResposta = false;
            cronometroGrafico.setVisibility(View.VISIBLE);

            for (ToggleButton button : tecladoBraile) {
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.teclado_braile_normal));
            }
        }

        novaPalavra();
    }

    @Override
    public void recomecarJogo() {
        for (int i = 0; i < vidas.length; i++) {
            vidas[i].setImageResource(R.drawable.ic_vida_ativa);
            vidasControle[i] = true;
        }

        errosConsecutivos = 0;
        acertosConsecutivos = 0;
        acertos = 0;
        erros = 0;

        txtAcertos.setText(String.valueOf(acertos));
        txtErros.setText(String.valueOf(erros));

        novaPalavra();
    }

    @Override
    public void voltarMenuPrincipal() {
        if (ativaTempo) {
            temporizador.cancel();
        }

        finish();
    }

    public void onCheckClick(View view) {
        tempoGrafico = 0;
        verificaResposta();
    }

    private void fimDeJogo() {
        if (ativaTempo) {
            temporizador.cancel();
        }
        new FimJogoFragment().abrir(getSupportFragmentManager());
    }

    public class TempoJogo extends TimerTask {

        @Override
        public void run() {

            tempoGrafico = (tempoGrafico + 1)%60;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cronometroGrafico.setMinute(tempoGrafico);
                }
            });

            if (tempoGrafico == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        verificaResposta();
                    }
                });
            }
        }
    }

}
