package com.example.sumi.brailler.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.sumi.brailler.JogoPrincipal;
import com.example.sumi.brailler.R;

/**
 * Created by kollins on 3/22/18.
 */

public class DificuldadeDialogFragment extends DialogFragment {

    private static final String DIALOG_TAG = "dificuldadeDialog";

    private CheckBox chkDificuldade;

    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dificuldade_menu_dialog_fragment, container, false);

        chkDificuldade = (CheckBox) view.findViewById(R.id.chbDificuldadeProgressiva);

        ((Button) view.findViewById(R.id.btnFacil)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JogoPrincipal.dificuldadeJogo = JogoPrincipal.FACIL;
                JogoPrincipal.dificuldadeProgressiva = chkDificuldade.isChecked();
                dismiss();
            }
        });

        ((Button) view.findViewById(R.id.btnMedio)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JogoPrincipal.dificuldadeJogo = JogoPrincipal.MEDIO;
                JogoPrincipal.dificuldadeProgressiva = chkDificuldade.isChecked();
                dismiss();
            }
        });

        ((Button) view.findViewById(R.id.btnDificil)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JogoPrincipal.dificuldadeJogo = JogoPrincipal.DIFICIL;
                JogoPrincipal.dificuldadeProgressiva = chkDificuldade.isChecked();
                dismiss();
            }
        });

        return view;
    }


    public void abrir(FragmentManager fm){
        this.fm = fm;
        if (fm.findFragmentByTag(DIALOG_TAG) == null){
            show(fm, DIALOG_TAG);
        }
    }
}
