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

package com.braille.souls.braille_souls;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.braille.souls.braille_souls.game.LearnGame;
import com.braille.souls.braille_souls.game.MultiplayerGame;
import com.braille.souls.braille_souls.game.SinglePlayerGame;
import com.braille.souls.braille_souls.visual_components.ProportionalImageView;

import java.util.ArrayList;

import static com.braille.souls.braille_souls.MainMenu.ANIMATION_TIME;

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
        Intent intent = new Intent(this, SinglePlayerGame.class);
        startActivity(intent);
    }

    public void onClickMultiButton(View view) {
        Intent intent = new Intent(this, MultiplayerGame.class);
        startActivity(intent);
    }

    private Context getContext() {
        return this;
    }

    public void onClickBack(View view) {
        finish();
    }
}
