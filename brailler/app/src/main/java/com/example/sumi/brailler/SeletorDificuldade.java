package com.example.sumi.brailler;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import static com.example.sumi.brailler.MenuPrincipal.PADRAO_ANIMACAO;

/**
 * Created by kollins on 3/26/18.
 */

public class SeletorDificuldade extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seletor_dificuldade);
    }

    private Context getContext() {
        return this;
    }

}
