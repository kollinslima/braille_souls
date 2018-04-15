package com.example.sumi.brailler.fragments;

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

import com.example.sumi.brailler.R;

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
