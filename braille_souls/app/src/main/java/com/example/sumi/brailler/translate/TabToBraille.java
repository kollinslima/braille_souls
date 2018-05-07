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

import com.example.sumi.brailler.R;

public class TabToBraille extends Fragment{

    private EditText input;
    private Button buttonTranslate;
    private GridView gridView;
    private View view;

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
                Toast.makeText(view.getContext(), getResources().getString(R.string.not_implemented_yet),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
