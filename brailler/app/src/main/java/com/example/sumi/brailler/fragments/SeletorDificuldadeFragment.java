package com.example.sumi.brailler.fragments;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.sumi.brailler.JogoPrincipal;
import com.example.sumi.brailler.R;

import java.util.ArrayList;

import static com.example.sumi.brailler.MenuPrincipal.PADRAO_ANIMACAO;
import static com.example.sumi.brailler.MenuPrincipal.TEMPO_ANIMACAO;

/**
 * Created by kollins on 3/26/18.
 */

public class SeletorDificuldadeFragment extends Fragment {

    private Intent itJogoPrincipal;
    private CheckBox chkDificuldade;

    private FrameLayout frameAnimacao;
    private ImageView auxImageView;
    private ArrayList<ImageView> vetorAnimacao;
    private boolean iniciaAnimacao;

    private ValueAnimator animator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itJogoPrincipal = new Intent(getActivity(), JogoPrincipal.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dificuldade_menu_activity, container, false);

        frameAnimacao = (FrameLayout) view.findViewById(R.id.frameAnimacao);
        vetorAnimacao = new ArrayList<ImageView>();
        iniciaAnimacao = true;

        //Adiciona primeiro elemento para poder pegar o tamanho
        auxImageView = new ImageView(getContext());
        auxImageView.setImageResource(PADRAO_ANIMACAO[0]);
        auxImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        auxImageView.setTranslationY(0);

        vetorAnimacao.add(0, auxImageView);

        frameAnimacao.addView(auxImageView);

        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(TEMPO_ANIMACAO);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float heightFrame = frameAnimacao.getHeight();
                final float heightBase = auxImageView.getHeight();
                final float translationY = (heightBase*PADRAO_ANIMACAO.length) * progress;

                Log.d("Animacao", "Tranlation: " + translationY);

                if (iniciaAnimacao) {
                    if (heightBase > 0) {
                        Log.d("Animacao", "Base: " + heightBase);

                        //Posiciona acima da tela para rolagem
                        vetorAnimacao.get(0).setTranslationY(-PADRAO_ANIMACAO.length*heightBase);

                        //Preenche a tela com o padrão
                        for (int i = 1; (i - PADRAO_ANIMACAO.length) * heightBase < heightFrame; i++) {
                            auxImageView = new ImageView(getContext());
                            auxImageView.setImageResource(PADRAO_ANIMACAO[i % 6]);
                            auxImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            auxImageView.setTranslationY((i - PADRAO_ANIMACAO.length) * heightBase);

                            vetorAnimacao.add(i, auxImageView);

                            frameAnimacao.addView(auxImageView);
                        }

                        iniciaAnimacao = false;
                    }
                } else {

                    for (int i = 0; i < vetorAnimacao.size(); i++) {
                        vetorAnimacao.get(i).setTranslationY(translationY + (i-PADRAO_ANIMACAO.length)*heightBase);
                    }

                }

//                animacao[0].setTranslationY(translationY);
//                animacao[1].setTranslationY(translationY - heightBase);
//                animacao[2].setTranslationY(translationY - 2*heightBase);
//                animacao[3].setTranslationY(translationY - 3*heightBase);
//                animacao[4].setTranslationY(translationY - 4*heightBase);
//                animacao[5].setTranslationY(translationY - 5*heightBase);

            }
        });
        animator.start();


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
