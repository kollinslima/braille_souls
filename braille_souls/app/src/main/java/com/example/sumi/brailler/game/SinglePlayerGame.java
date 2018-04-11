package com.example.sumi.brailler.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sumi.brailler.MainMenu;
import com.example.sumi.brailler.R;
import com.example.sumi.brailler.fragments.MultiplayerChooseFragment;
import com.example.sumi.brailler.fragments.PauseFragment;
import com.example.sumi.brailler.fragments.PlayerWaitFragment;
import com.example.sumi.brailler.fragments.PointTableMultiplayer;
import com.example.sumi.brailler.visual_components.ProportionalImageView;
import com.plusquare.clockview.ClockView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class SinglePlayerGame extends AppCompatActivity
        implements PauseFragment.onDismissListener {

    public static final String GAME_TAG_LOG = "gameLog";

    private int speedDown = 5000;

    private final double TIMER_SET = 1;
    private final int TIMER_INTERVAL = (int) (TIMER_SET * 1000);   //ms
    private Timer timerAddSymbol0;

    private String randomSymbol;

    private ToggleButton[] brailleKeyboard = new ToggleButton[6];

    private Button pauseButton;

    private FrameLayout imageBox0, imageBox1, imageBox2, imageBox3;

//    private TextView textHits, textMiss;
//    private int hitCount, missCount;

    private boolean backButtonFlag, gameOverFlag, isGameRunning;

    private Random random;

    private ValueAnimator imageAnimator0, imageAnimator1, imageAnimator2, imageAnimator3;

    private ArrayList<TextView> textBox0;
    private ArrayList<Float> transactionBox0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer_game);

        random = new Random();

        backButtonFlag = false;
        gameOverFlag = false;

        brailleKeyboard[0] = (ToggleButton) findViewById(R.id.braille_button_1x1);
        brailleKeyboard[1] = (ToggleButton) findViewById(R.id.braille_button_1x2);
        brailleKeyboard[2] = (ToggleButton) findViewById(R.id.braille_button_2x1);
        brailleKeyboard[3] = (ToggleButton) findViewById(R.id.braille_button_2x2);
        brailleKeyboard[4] = (ToggleButton) findViewById(R.id.braille_button_3x1);
        brailleKeyboard[5] = (ToggleButton) findViewById(R.id.braille_button_3x2);

        imageBox0 = (FrameLayout) findViewById(R.id.imageBox0);
        imageBox1 = (FrameLayout) findViewById(R.id.imageBox1);
        imageBox2 = (FrameLayout) findViewById(R.id.imageBox2);
        imageBox3 = (FrameLayout) findViewById(R.id.imageBox3);

        textBox0 = new ArrayList<TextView>();
        transactionBox0 = new ArrayList<Float>();

        isGameRunning = false;

        timerAddSymbol0 = new Timer();
        timerAddSymbol0.scheduleAtFixedRate(new AddNewSymbol0(), TIMER_INTERVAL, TIMER_INTERVAL);

        setAnimation();
    }

    private void setAnimation() {

        imageAnimator0 = ValueAnimator.ofFloat(0.0f, 1.0f);
        imageAnimator0.setRepeatCount(ValueAnimator.INFINITE);
        imageAnimator0.setInterpolator(new LinearInterpolator());
        imageAnimator0.setDuration(speedDown);
        imageAnimator0.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                final float progress = (float) animation.getAnimatedValue();
                final float heightFrame = imageBox0.getHeight();
                float translationY = heightFrame * progress;

                for (int i = 0; i < textBox0.size(); i++) {
                    textBox0.get(i).setTranslationY(translationY - transactionBox0.get(i));
                }


            }
        });
        imageAnimator0.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                textBox0.clear();
                transactionBox0.clear();
                imageBox0.removeAllViews();
            }
        });
        imageAnimator0.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if ((!(backButtonFlag || gameOverFlag)) && isGameRunning) {
            pauseGame();
        }
    }

    private void pauseGame() {
        new PauseFragment().showDialog(getSupportFragmentManager());
    }

    @Override
    public void continueGameFragment() {

    }

    @Override
    public void backToMainMenu() {
        gameOverFlag = true;
        finish();
    }

    private Context getContext() {
        return this;
    }

    private class AddNewSymbol0 extends TimerTask {

        @Override
        public void run() {

            if (random.nextBoolean()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView auxTextView = new TextView(getContext());
                        auxTextView.setText("A");
                        auxTextView.setTextColor(Color.BLACK);
                        auxTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                        auxTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                        auxTextView.setTranslationY(0);

                        if (textBox0.size() > 0) {
                            transactionBox0.add(textBox0.get(0).getTranslationY());
                        } else {
                            imageAnimator0.setCurrentPlayTime(0);
                            transactionBox0.add(0f);
                        }

                        textBox0.add(auxTextView);
                        imageBox0.addView(auxTextView);
                    }
                });
            }
        }
    }

}
