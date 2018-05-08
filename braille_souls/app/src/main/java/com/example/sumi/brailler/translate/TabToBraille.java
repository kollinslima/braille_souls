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

import com.example.sumi.brailler.MainMenu;
import com.example.sumi.brailler.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabToBraille extends Fragment{

    private EditText input;
    private String income;
    private Button buttonTranslate;
    private GridView gridView;
    private View view;
    private ArrayList<String> brailleCodes;
    private ArrayList<String> brailleDots;

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

                brailleCodes = new ArrayList<String>();
                brailleDots = new ArrayList<String>();
                income = input.getText().toString();
                income = income.toUpperCase();
                putOnBrailleCodes();
                putOnBrailleDots();
                GridBrailleAdapter mAdapter = new GridBrailleAdapter(view.getContext(), brailleDots);
                gridView.setAdapter(mAdapter);
            }
        });
    }

    private void putOnBrailleDots() {
        String aux = "";
        if(brailleCodes.isEmpty()) return;
        for(int i = 0; i < brailleCodes.size(); i ++){
            int it;
            String Bcode = brailleCodes.get(i);
            //Toast.makeText(view.getContext(), Bcode, Toast.LENGTH_SHORT).show();
            for(it = 0; it < 6; it++){
                aux = aux + addDotNDot(Bcode.charAt(it));
                if(it%2 != 0){
                    aux = aux + "\n";
                }
            }
            //Toast.makeText(view.getContext(), aux, Toast.LENGTH_SHORT).show();
            brailleDots.add(aux);
            aux = "";
//            for(int ii = 0; ii < 6; ii++){
//                if(Bcode.charAt(ii) == '1' && ii < 2){
//                    aux += "◉ ";
//                    if(ii == 1) aux += "\n";
//                }else if (Bcode.charAt(ii) == '0' && ii < 2){
//                    aux += "  ";
//                    if(ii == 1) aux += "\n";
//                }else if (Bcode.charAt(ii) == '1' && ii < 4){
//                    aux += "◉ ";
//                    if(ii == 3) aux += "\n";
//                }else if (Bcode.charAt(ii) == '0' && ii < 4){
//                    aux += "◉ ";
//                    if(ii == 3) aux += "\n";
//                }else if (Bcode.charAt(ii) == '1' && ii < 6){
//                    aux += "◉ ";
//                    if(ii == 5) aux += "\n";
//                }else if (Bcode.charAt(ii) == '0' && ii < 6){
//                    aux += "◉ ";
//                    if(ii == 5) aux += "\n";
//                }
//            }
        }
    }

    private void putOnBrailleCodes() {
        int size = income.length();
        if(size == 0){
            Toast.makeText(view.getContext(), getResources().getString(R.string.toast_empty), Toast.LENGTH_SHORT).show();
        }
        for(int i = 0; i < size; i++){
            if(MainMenu.text_to_braille.containsKey(""+ income.charAt(i))){
                brailleCodes.add(MainMenu.text_to_braille.get(""+ income.charAt(i)));
            }else if(income.charAt(i) == ' '){
                brailleCodes.add("      ");
            }else{
                brailleCodes.add("???????");
            }

            //Toast.makeText(view.getContext(), MainMenu.text_to_braille.get(""+ income.charAt(i)), Toast.LENGTH_SHORT).show();
        }
    }

    private String addDotNDot(char binCode){
        if(binCode == '1'){
            return "◉ ";
        }else if(binCode == '?'){
            return "? ";
        }else{
            return "  ";
        }
    }




}
