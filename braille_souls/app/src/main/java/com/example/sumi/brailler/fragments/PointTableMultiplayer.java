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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sumi.brailler.R;

import java.util.ArrayList;

/**
 * Created by kollins on 3/22/18.
 */

public class PointTableMultiplayer extends DialogFragment {

    public static final String DIALOG_TAG = "pointTableDialog";

    private ListView pointList;
    private ArrayAdapter<String> pointAdapter;
    private ArrayList<Integer> points;
    private Button closeButton, mainMenuButton;

    private boolean isEndGame;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View layout = inflater.inflate(R.layout.fragment_table_points_multiplayer, container, false);

        ArrayList<String> pointString = new ArrayList<String>();

        int loopSize = points.size();
        for (int i = 0; i < loopSize; i++) {
            int greater_value = points.get(0);

            for (int point : points) {
                if (point > greater_value) {
                    greater_value = point;
                }
            }

            pointString.add("Player " + (points.indexOf(greater_value) + 1) + ": " + greater_value);
            points.set(points.indexOf(greater_value), Integer.MIN_VALUE);
        }

        pointAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, pointString);

        pointList = (ListView) layout.findViewById(R.id.pointList);
        pointList.setAdapter(pointAdapter);

        closeButton = (Button) layout.findViewById(R.id.closeButton);
        mainMenuButton = (Button) layout.findViewById(R.id.mainMenuButton);

        if (!isEndGame) {
            mainMenuButton.setVisibility(View.GONE);
            closeButton.setText(getResources().getString(R.string.close_button));

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        } else {
            closeButton.setText(getResources().getString(R.string.close_button_end));
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = getActivity();
                    if (activity instanceof PointTableMultiplayer.onDismissListener) {
                        ((PointTableMultiplayer.onDismissListener) activity).resetGame();
                    }
                    dismiss();
                }
            });

            mainMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = getActivity();
                    if (activity instanceof PointTableMultiplayer.onDismissListener) {
                        ((PointTableMultiplayer.onDismissListener) activity).backToMainMenu();
                    }
                    dismiss();
                }
            });
        }

        return layout;
    }


    public void showDialog(FragmentManager fm, ArrayList<Integer> points, boolean isEndGame) {
        if (fm.findFragmentByTag(DIALOG_TAG) == null) {
            show(fm, DIALOG_TAG);
            this.points = points;
            this.isEndGame = isEndGame;
        }
    }

    public interface onDismissListener {
        void resetGame();
        void backToMainMenu();
    }

}
