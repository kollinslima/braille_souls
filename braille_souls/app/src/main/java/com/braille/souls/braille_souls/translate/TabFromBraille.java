/*
 * Copyright 2018
 * Kollins Lima (kollins.lima@gmail.com)
 * Otávio Sumi (otaviosumi@hotmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.braille.souls.braille_souls.translate;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.braille.souls.braille_souls.MainMenu;
import com.braille.souls.braille_souls.R;

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
                    Toast.makeText(view.getContext(), getResources().getString(R.string.toast_unknown), Toast.LENGTH_SHORT).show();
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    result.setText(Html.fromHtml(result.getText() + answer, Html.FROM_HTML_MODE_COMPACT));
                } else{
                    result.setText(Html.fromHtml(result.getText() + answer));

                }
            }
        });

        spaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    result.setText(Html.fromHtml(result.getText() + " ", Html.FROM_HTML_MODE_COMPACT));
                } else{
                    result.setText(Html.fromHtml(result.getText() + " "));

                }
            }
        });
    }





}
