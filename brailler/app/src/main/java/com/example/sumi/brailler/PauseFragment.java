package com.example.sumi.brailler;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by kollins on 3/22/18.
 */

public class PauseFragment extends DialogFragment {

    public static final String DIALOG_TAG = "pauseDialog";

    private Button botaoContinua, botaoDificuldade, botaoMenuPrincipal;

    private String dificuldadeNivel;
    private boolean dificuldadeProgressiva;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.botao_pause_menu, container, false);

        botaoContinua = (Button) layout.findViewById(R.id.botaoContinuarJogo);
        botaoContinua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof onDismissListener) {
                    ((onDismissListener) activity).continuarJogoFragment();
                }
                dismiss();
            }
        });

        botaoDificuldade = (Button) layout.findViewById(R.id.botaoTrocaDificuldade);
        botaoDificuldade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DificuldadeDialogFragment().abrir(getActivity().getSupportFragmentManager());
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
        void continuarJogoFragment();
        void voltarMenuPrincipal();
    }

}
