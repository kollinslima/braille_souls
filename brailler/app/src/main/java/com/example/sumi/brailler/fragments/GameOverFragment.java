package com.example.sumi.brailler.fragments;

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

import com.example.sumi.brailler.R;

/**
 * Created by kollins on 3/22/18.
 */

public class GameOverFragment extends DialogFragment {

    private static final String DIALOG_TAG = "gameOverDialog";

    private Button playAgainButton, mainMenuButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View layout = inflater.inflate(R.layout.fragment_game_over, container, false);

        playAgainButton = (Button) layout.findViewById(R.id.buttonResetGame);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof GameOverFragment.onDismissListener) {
                    ((GameOverFragment.onDismissListener) activity).resetGame();
                }
                dismiss();
            }
        });

        mainMenuButton = (Button) layout.findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof GameOverFragment.onDismissListener) {
                    ((GameOverFragment.onDismissListener) activity).backToMainMenu();
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
        void resetGame();
        void backToMainMenu();
    }
}
