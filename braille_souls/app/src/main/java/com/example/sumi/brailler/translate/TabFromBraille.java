package com.example.sumi.brailler.translate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sumi.brailler.MainMenu;
import com.example.sumi.brailler.R;

import java.util.ArrayList;
import java.util.Collection;

public class TabFromBraille extends Fragment{

    private TextView result;
    private ToggleButton[] brailleKeyboard = new ToggleButton[6];
    private Button spaceButton, addButton;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_from_braille, container, false);
        initializer();
        return view;
    }

    private void initializer() {
        result = (TextView)view.findViewById(R.id.fieldResult);
        brailleKeyboard[0] = (ToggleButton) view.findViewById(R.id.braille_button_1x1);
        brailleKeyboard[1] = (ToggleButton) view.findViewById(R.id.braille_button_1x2);
        brailleKeyboard[2] = (ToggleButton) view.findViewById(R.id.braille_button_2x1);
        brailleKeyboard[3] = (ToggleButton) view.findViewById(R.id.braille_button_2x2);
        brailleKeyboard[4] = (ToggleButton) view.findViewById(R.id.braille_button_3x1);
        brailleKeyboard[5] = (ToggleButton) view.findViewById(R.id.braille_button_3x2);
        spaceButton = (Button)view.findViewById(R.id.espace_button);
        addButton = (Button)view.findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] answerList;
                String answer = "";
                int size;

                for (ToggleButton button : brailleKeyboard) {
                    if (button.isChecked()) {
                        answer = answer.concat("1");
                    } else {
                        answer = answer.concat("0");
                    }
                    button.setChecked(false);
                }
                if(!MainMenu.braille_to_text.containsKey(answer)){
                    return;
                }
                size = MainMenu.braille_to_text.get(answer).size();
                answerList = MainMenu.braille_to_text.get(answer).toArray(new String[size]);
                answer = "";
                for(int x = 0; x < size; x++){
                    if(Character.isDigit(answerList[x].charAt(0)) && x == 0){
                        answer = answer + answerList[x] + " / ";
                    }else if(Character.isDigit(answerList[x].charAt(0)) && x != 0){
                        answer = answer + "/" + answerList[x];
                    }else{
                        answer = answer + answerList[x];
                    }

                }
                result.setText(result.getText() + answer);
            }
        });

        spaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText(result.getText() + " ");
            }
        });
    }





}
