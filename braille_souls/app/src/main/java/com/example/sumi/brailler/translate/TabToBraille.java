package com.example.sumi.brailler.translate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.sumi.brailler.R;

import java.util.ArrayList;

public class TabToBraille extends Fragment{

    private EditText input;
    private String income;
    private Button buttonTranslate;
    private GridView gridView;
    private View view;
    private ArrayList<String> brailleCodes;
    private String[] brailleDots;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_to_braille, container, false);
        inicializer();
        return view;
    }

    private void inicializer(){
        input = (EditText)view.findViewById(R.id.translatorTextFieldInput);
        buttonTranslate = (Button)view.findViewById(R.id.translateButton);
        gridView = (GridView)view.findViewById(R.id.gridviewToBraille);


        buttonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                income = input.getText().toString();    //working
                income = income.toUpperCase();          //

                //inputAsBraille(income);
                Toast.makeText(view.getContext(), income,Toast.LENGTH_SHORT).show();
//                setBrailleCodesOnGrid(brailleCodes);
//                GridBrailleAdapter mAdapter = new GridBrailleAdapter(view.getContext(), brailleDots);
//                gridView.setAdapter(mAdapter);

            }
        });
    }

    private int inputAsBraille(String aux){
        return aux.length();

    }

    private void setBrailleCodesOnGrid(String[] brailleCodes){
        int size = brailleCodes.length;
        brailleDots = new String[size];
        for(int i = 0; i < size; i++){
            String aux = brailleCodes[i];
            brailleDots[i] = "";
            for(int ii = 0; ii < 6; ii++){
                if(aux.charAt(ii) == 1 && ii < 2){
                    brailleDots[i] = brailleDots[i] + "◉ ";
                }else if(aux.charAt(ii) == 0 && ii < 2){
                    brailleDots[i] = brailleDots[i] + "  ";
                }else if(aux.charAt(ii) == 1 && ii < 4){
                    if(ii == 2)  brailleDots[i] = brailleDots[i] + "\n";
                    brailleDots[i] = brailleDots[i] + "◉ ";
                }else if(aux.charAt(ii) == 0 && ii < 4){
                    if(ii == 2)  brailleDots[i] = brailleDots[i] + "\n";
                    brailleDots[i] = brailleDots[i] + "  ";
                }else if(aux.charAt(ii) == 1 && ii < 6){
                    if(ii == 4)  brailleDots[i] = brailleDots[i] + "\n";
                    brailleDots[i] = brailleDots[i] + "◉ ";
                }else if(aux.charAt(ii) == 0 && ii < 6){
                    if(ii == 4)  brailleDots[i] = brailleDots[i] + "\n";
                    brailleDots[i] = brailleDots[i] + "  ";
                }
            }
        }
    }
}
