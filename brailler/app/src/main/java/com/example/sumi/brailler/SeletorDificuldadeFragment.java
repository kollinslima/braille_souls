package com.example.sumi.brailler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Created by kollins on 3/26/18.
 */

public class SeletorDificuldadeFragment extends Fragment {

    private Intent itJogoPrincipal;
    private CheckBox chkDificuldade;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itJogoPrincipal = new Intent(getActivity(), JogoPrincipal.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dificuldade_menu, container, false);

        chkDificuldade = (CheckBox) view.findViewById(R.id.chbDificuldadeProgressiva);

        ((Button) view.findViewById(R.id.btnFacil)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itJogoPrincipal.putExtra(JogoPrincipal.SELETOR_DIFICULDADE, JogoPrincipal.FACIL);
                iniciaJogo();
            }
        });

        ((Button) view.findViewById(R.id.btnMedio)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itJogoPrincipal.putExtra(JogoPrincipal.SELETOR_DIFICULDADE, JogoPrincipal.MEDIO);
                iniciaJogo();
            }
        });

        ((Button) view.findViewById(R.id.btnDificil)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itJogoPrincipal.putExtra(JogoPrincipal.SELETOR_DIFICULDADE, JogoPrincipal.DIFICIL);
                iniciaJogo();
            }
        });

        return view;
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
