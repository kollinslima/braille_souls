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
import android.widget.TextView;

import com.example.sumi.brailler.R;

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
            show(fm, DIALOG_TAG);
            this.playerTurn = playerTurn;
        }
    }

    public interface onDismissListener {
        void startTurn();
    }

}
