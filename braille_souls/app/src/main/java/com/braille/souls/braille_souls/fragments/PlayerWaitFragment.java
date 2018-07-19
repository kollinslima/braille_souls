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

package com.braille.souls.braille_souls.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.braille.souls.braille_souls.R;

/**
 * Created by kollins on 3/22/18.
 */

public class PlayerWaitFragment extends DialogFragment {

    public static final String DIALOG_TAG = "playerWaitDialog";

    private int playerTurn;
    private Button startButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View layout = inflater.inflate(R.layout.fragment_player_wait, container, false);

        ((TextView) layout.findViewById(R.id.playerTurn)).setText(getResources().getString(R.string.player_turn, playerTurn));

        startButton = (Button) layout.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof PlayerWaitFragment.onDismissListener) {
                    ((PlayerWaitFragment.onDismissListener) activity).startTurn();
                }
                dismiss();
            }
        });

        return layout;
    }


    public void showDialog(FragmentManager fm, int playerTurn) {
        if (fm.findFragmentByTag(DIALOG_TAG) == null) {
            setCancelable(false);
            show(fm, DIALOG_TAG);
            this.playerTurn = playerTurn;
        }
    }

    public interface onDismissListener {
        void startTurn();
    }

}
