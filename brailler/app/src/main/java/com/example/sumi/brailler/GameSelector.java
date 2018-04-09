package com.example.sumi.brailler;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.example.sumi.brailler.database.DataBaseHelper;
import com.example.sumi.brailler.game.LearnGame;
import com.example.sumi.brailler.game.LearnModeGame;
import com.example.sumi.brailler.game.MultiplayerGame;
import com.example.sumi.brailler.translate.TabbedTranslator;
import com.example.sumi.brailler.user_profile.UserProfile;
import com.example.sumi.brailler.user_profile.UserProfileActivity;
import com.example.sumi.brailler.visual_components.ProportionalImageView;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.sumi.brailler.MainMenu.ANIMATION_TIME;

public class GameSelector extends AppCompatActivity {

    public static final int[] ANIMATION_PATTERN = {
            R.drawable.ic_background_animation_1,
            R.drawable.ic_background_animation_2,
            R.drawable.ic_background_animation_3,
            R.drawable.ic_background_animation_4,
            R.drawable.ic_background_animation_5,
            R.drawable.ic_background_animation_6
    };

    private FrameLayout animationFrame;
    private ProportionalImageView auxImageView;
    private ArrayList<ProportionalImageView> animationVector;
    private boolean animationInitFlag;

    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selector);

        animationFrame = (FrameLayout) findViewById(R.id.animationFrame);
        animationVector = new ArrayList<ProportionalImageView>();
        animationInitFlag = true;

        //Add first element to get size later
        auxImageView = new ProportionalImageView(getContext());
        auxImageView.setImageResource(ANIMATION_PATTERN[0]);
        auxImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        auxImageView.setTranslationY(0);

        animationVector.add(0, auxImageView);

        animationFrame.addView(auxImageView);

        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(ANIMATION_TIME);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float heightFrame = animationFrame.getHeight();
                final float heightBase = auxImageView.getHeight();
                final float translationY = (heightBase* ANIMATION_PATTERN.length) * progress;

                if (animationInitFlag) {
                    if (heightBase > 0) {

                        //set position to scroll
                        animationVector.get(0).setTranslationY(-ANIMATION_PATTERN.length*heightBase);

                        //fill screen
                        for (int i = 1; (i - ANIMATION_PATTERN.length) * heightBase < heightFrame; i++) {
                            auxImageView = new ProportionalImageView(getContext());
                            auxImageView.setImageResource(ANIMATION_PATTERN[i % 6]);
                            auxImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            auxImageView.setTranslationY((i - ANIMATION_PATTERN.length) * heightBase);

                            animationVector.add(i, auxImageView);

                            animationFrame.addView(auxImageView);
                        }

                        animationInitFlag = false;
                    }
                } else {

                    for (int i = 0; i < animationVector.size(); i++) {
                        animationVector.get(i).setTranslationY(translationY + (i- ANIMATION_PATTERN.length)*heightBase);
                    }

                }

            }
        });
        animator.start();

    }

    public void onClickSingleButton(View view) {
        Intent intent = new Intent(this, LearnModeGame.class);
        startActivity(intent);
    }

    public void onClickMultiButton(View view) {
        Intent intent = new Intent(this, MultiplayerGame.class);
        startActivity(intent);
    }

    private Context getContext() {
        return this;
    }

}
