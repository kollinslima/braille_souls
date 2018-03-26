package com.example.sumi.brailler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

/**
 * Created by kollins on 3/26/18.
 */

public class SeletorDificuldade extends AppCompatActivity {

    private Intent itJogoPrincipal;
    private CheckBox chkDificuldade;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seletor_dificuldade);

        itJogoPrincipal = new Intent(this, JogoPrincipal.class);

        chkDificuldade = (CheckBox) findViewById(R.id.chbDificuldadeProgressiva);
    }

    public void onFacilSelecionado(View view) {
        itJogoPrincipal.putExtra(JogoPrincipal.SELETOR_DIFICULDADE, JogoPrincipal.FACIL);
        iniciaJogo();
    }

    public void onMedioSelecionado(View view) {
        itJogoPrincipal.putExtra(JogoPrincipal.SELETOR_DIFICULDADE, JogoPrincipal.MEDIO);
        iniciaJogo();
    }

    public void onDificilSelecionado(View view) {
        itJogoPrincipal.putExtra(JogoPrincipal.SELETOR_DIFICULDADE, JogoPrincipal.DIFICIL);
        iniciaJogo();
    }

    private void iniciaJogo(){
        if (chkDificuldade.isChecked()){
            itJogoPrincipal.putExtra(JogoPrincipal.DIFICULDADE_PROGRESSIVA, true);
        } else {
            itJogoPrincipal.putExtra(JogoPrincipal.DIFICULDADE_PROGRESSIVA, false);
        }
        startActivity(itJogoPrincipal);
    }
}
