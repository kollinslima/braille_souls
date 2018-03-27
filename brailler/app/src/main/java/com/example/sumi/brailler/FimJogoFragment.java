package com.example.sumi.brailler;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by kollins on 3/22/18.
 */

public class FimJogoFragment extends DialogFragment {

    private static final String DIALOG_TAG = "fimJogoDialog";

    private Button botaoJogarNovamente, botaoMenuPrincipal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.layout_fim_de_jogo, container, false);

        botaoJogarNovamente = (Button) layout.findViewById(R.id.botaoReiniciaJogo);
        botaoJogarNovamente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof onDismissListener) {
                    ((onDismissListener) activity).recomecarJogo();
                }
                dismiss();
            }
        });

        botaoMenuPrincipal = (Button) layout.findViewById(R.id.botaoMenuPrincipal);
        botaoMenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof onDismissListener) {
                    ((onDismissListener) activity).voltarMenuPrincipal();
                }
                dismiss();
            }
        });

        return layout;
    }


    public void abrir(FragmentManager fm){
        if (fm.findFragmentByTag(DIALOG_TAG) == null){
            setCancelable(false);
            show(fm, DIALOG_TAG);
        }
    }

    public interface onDismissListener
    {
        void recomecarJogo();
        void voltarMenuPrincipal();
    }
}
