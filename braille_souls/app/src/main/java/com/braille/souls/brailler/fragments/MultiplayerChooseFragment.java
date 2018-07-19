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

package com.braille.souls.brailler.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.braille.souls.brailler.R;

/**
 * Created by kollins on 3/22/18.
 */

public class MultiplayerChooseFragment extends DialogFragment {

    public static final String DIALOG_TAG = "multiplayerDialog";

    private Button buttonUp, buttonDown, startButton;
    private TextView numberOfPlayers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View layout = inflater.inflate(R.layout.fragment_multiplayer_choose, container, false);

        numberOfPlayers = (TextView) layout.findViewById(R.id.playerNumber);
        numberOfPlayers.setText("1");

        buttonUp = (Button) layout.findViewById(R.id.buttonUp);
        buttonUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    buttonUp.setBackgroundResource(R.drawable.arrow_up_pressed);
                    numberOfPlayers.setText(String.valueOf(Integer.valueOf(numberOfPlayers.getText().toString()) + 1));
                } else {
                    buttonUp.setBackgroundResource(R.drawable.arrow_up);
                }
                return true;
            }
        });

        buttonDown = (Button) layout.findViewById(R.id.buttonDown);
        buttonDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    buttonDown.setBackgroundResource(R.drawable.arrow_down_pressed);
                    int newNumber = Integer.valueOf(numberOfPlayers.getText().toString()) - 1;
                    if (newNumber > 0) {
                        numberOfPlayers.setText(String.valueOf(newNumber));
                    }
                } else {
                    buttonDown.setBackgroundResource(R.drawable.arrow_down);
                }
                return true;
            }
        });

        startButton = (Button) layout.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof MultiplayerChooseFragment.onDismissListener) {
                    ((MultiplayerChooseFragment.onDismissListener) activity).startGame(Integer.valueOf(numberOfPlayers.getText().toString()));
                }
                dismiss();
            }
        });

        return layout;
    }

    public void showDialog(FragmentManager fm) {
        if (fm.findFragmentByTag(DIALOG_TAG) == null) {
            show(fm, DIALOG_TAG);
        }
    }

    public interface onDismissListener {
        void startGame(int numberOfPlayers);
    }

}
