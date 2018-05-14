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

package com.example.sumi.brailler.fragments;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.sumi.brailler.R;

/**
 * Created by kollins on 3/22/18.
 */

public class PauseFragment extends DialogFragment {

    public static final String DIALOG_TAG = "pauseDialog";

    private Button continueButton, mainMenuButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View layout = inflater.inflate(R.layout.fragment_pause_button_menu, container, false);

        continueButton = (Button) layout.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof onDismissListener) {
                    ((onDismissListener) activity).continueGameFragment();
                }
                dismiss();
            }
        });

        mainMenuButton = (Button) layout.findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof onDismissListener) {
                    ((onDismissListener) activity).backToMainMenu();
                }
                dismiss();
            }
        });

        return layout;
    }


    public void showDialog(FragmentManager fm){
        if (fm.findFragmentByTag(DIALOG_TAG) == null){
            setCancelable(false);
            show(fm, DIALOG_TAG);
        }
    }

    public interface onDismissListener
    {
        void continueGameFragment();
        void backToMainMenu();
    }

}
