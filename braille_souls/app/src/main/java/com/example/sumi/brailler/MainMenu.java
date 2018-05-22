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

package com.example.sumi.brailler;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.sumi.brailler.database.DataBaseHelper;
import com.example.sumi.brailler.game.LearnGame;
import com.example.sumi.brailler.tutorial.LearnGameTutorial;
import com.example.sumi.brailler.translate.TranslatorActivity;
import com.example.sumi.brailler.user_profile.UserProfile;
import com.example.sumi.brailler.user_profile.UserProfileActivity;
import com.example.sumi.brailler.visual_components.ProportionalImageView;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainMenu extends AppCompatActivity {

    public static final int[] ANIMATION_PATTERN = {
            R.drawable.ic_background_animation_1,
            R.drawable.ic_background_animation_2,
            R.drawable.ic_background_animation_3,
            R.drawable.ic_background_animation_4,
            R.drawable.ic_background_animation_5,
            R.drawable.ic_background_animation_6
    };

    public static final long ANIMATION_TIME = 5000;

    public static UserProfile user;

    public static final String DATABASE_TAG = "DatabaseTest";
    public static final HashMap<String, String> text_to_braille = new HashMap<String, String>();
    public static final Multimap<String, String> braille_to_text = ArrayListMultimap.create();

    private FrameLayout animationFrame;
    private ProportionalImageView auxImageView;
    private ArrayList<ProportionalImageView> animationVector;
    private boolean animationInitFlag;

    private ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        user = new UserProfile(this);

        //Remove Dictionary and Profile button
        ((Button) findViewById(R.id.btnDictionaty)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.btnProfile)).setVisibility(View.GONE);

        loadDataBase();

        TextView appName = findViewById(R.id.appNameMainMenu);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(appName, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);


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

    private void loadDataBase() {

        DataBaseHelper mDBHelper = new DataBaseHelper(this);
        SQLiteDatabase mDb = null;

        try {
            mDBHelper.updateDataBase();
            mDb = mDBHelper.getWritableDatabase();

            Cursor cursor = mDb.query("brailleTable", null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {

                    text_to_braille.put(cursor.getString(cursor.getColumnIndex("PlainText")),
                            cursor.getString(cursor.getColumnIndex("Braille")));

                    braille_to_text.put(cursor.getString(cursor.getColumnIndex("Braille")),
                            cursor.getString(cursor.getColumnIndex("PlainText")));

//                    Log.d(DATABASE_TAG, "Braille: " + cursor.getString(cursor.getColumnIndex("Braille"))
//                            + " - Text: " + braille_to_text.get(cursor.getString(cursor.getColumnIndex("Braille"))));
                } while (cursor.moveToNext());
            }

        } catch (SQLException mSQLException) {
            throw mSQLException;
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        } finally {
            mDb.close();
        }
    }

    public void onClickLearnButton(View view) {

        Intent intent = null;
        if (user.isFirstTimeLearnMode()){
            intent = new Intent(this, LearnGameTutorial.class);
        } else {
            intent = new Intent(this, LearnGame.class);
        }
        startActivity(intent);
    }

    public void onClickPlayButton(View view) {
        Intent intent = new Intent(this, GameSelector.class);
        startActivity(intent);
    }

    public void onClickDictionatyButton(View view) {
        Intent intent = new Intent(this, TranslatorActivity.class);
        startActivity(intent);
//        Intent intent = new Intent(this, TabbedTranslator.class);
//        startActivity(intent);
    }

    public void onClickProfileButton(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void onClickExitButton(View view) {
        finish();
    }

    private Context getContext() {
        return this;
    }

}
