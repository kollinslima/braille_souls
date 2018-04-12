package com.example.sumi.brailler.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import com.example.sumi.brailler.fragments.PauseFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SinglePlayerGame extends AppCompatActivity
        implements PauseFragment.onDismissListener, View.OnClickListener {

    public static final String GAME_TAG_LOG = "gameLog";

    private int speedDown = 10000;

    private final double TIMER_SET = 1;
    private final int TIMER_INTERVAL = (int) (TIMER_SET * 1000);   //ms
    private Timer timerAddSymbol0;

    private String randomSymbol;

    private ToggleButton[] brailleKeyboard = new ToggleButton[6];

    private Button pauseButton;

    private FrameLayout[] frameBox = new FrameLayout[4];
//    private FrameLayout imageBox0, imageBox1, imageBox2, imageBox3;

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

        ((Button) findViewById(R.id.check_button)).setOnClickListener(this);

        brailleKeyboard[0] = (ToggleButton) findViewById(R.id.braille_button_1x1);
        brailleKeyboard[1] = (ToggleButton) findViewById(R.id.braille_button_1x2);
        brailleKeyboard[2] = (ToggleButton) findViewById(R.id.braille_button_2x1);
        brailleKeyboard[3] = (ToggleButton) findViewById(R.id.braille_button_2x2);
        brailleKeyboard[4] = (ToggleButton) findViewById(R.id.braille_button_3x1);
        brailleKeyboard[5] = (ToggleButton) findViewById(R.id.braille_button_3x2);

        frameBox[0] = (FrameLayout) findViewById(R.id.frameBox0);
        frameBox[1] = (FrameLayout) findViewById(R.id.frameBox1);
        frameBox[2] = (FrameLayout) findViewById(R.id.frameBox2);
        frameBox[3] = (FrameLayout) findViewById(R.id.frameBox3);

        textBox0 = new ArrayList<TextView>();
        transactionBox0 = new ArrayList<Float>();

        isGameRunning = false;

        timerAddSymbol0 = new Timer();
        timerAddSymbol0.scheduleAtFixedRate(new AddNewSymbol0(), TIMER_INTERVAL, TIMER_INTERVAL);

        setAnimation();
    }

    private void setAnimation() {

        imageAnimator0 = ValueAnimator.ofFloat(0.0f, 1.0f);
//        imageAnimator0.setRepeatCount(ValueAnimator.INFINITE);
        imageAnimator0.setRepeatCount(0);
        imageAnimator0.setInterpolator(new LinearInterpolator());
        imageAnimator0.setDuration(speedDown);
        imageAnimator0.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                final float progress = (float) animation.getAnimatedValue();
                final float heightFrame = frameBox[0].getHeight();

                if (textBox0.size() > 0) {
                    if ((progress - transactionBox0.get(0)) >= 1) {
                        imageAnimator0.cancel();
                    } else {
                        final float baseHeigh = textBox0.get(0).getHeight();

                        if (baseHeigh != 0) {
                            final float efectiveHeigh = heightFrame - baseHeigh;

                            for (int i = 0; i < textBox0.size(); i++) {
                                textBox0.get(i).setTranslationY(efectiveHeigh * (progress - transactionBox0.get(i)));
                            }
                        }
                    }
                }
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

    @Override
    public void onClick(View v) {
        String answer = "";

        for (ToggleButton button : brailleKeyboard) {
            if (button.isChecked()) {
                answer = answer.concat("1");
            } else {
                answer = answer.concat("0");
            }

            button.setChecked(false);
        }

        try {
            boolean removeOne = false;
            for (int i = 0; i < textBox0.size() && !removeOne; i++) {
                if (MainMenu.braille_to_text.get(answer).contains(textBox0.get(i).getText().toString())) {
                    ((ViewGroup) textBox0.get(i).getParent()).removeView(textBox0.get(i));
                    textBox0.remove(textBox0.indexOf(textBox0.get(i)));
                    transactionBox0.remove(i);

                    final float oldAnimationValue = (float) imageAnimator0.getAnimatedValue();
                    imageAnimator0.setCurrentPlayTime(0);

                    for (int j = 0; j < transactionBox0.size(); j++){
                        transactionBox0.set(j, -(oldAnimationValue-transactionBox0.get(j)));
                    }

                    removeOne = true;
                }
            }
        } catch (NullPointerException e) {
            Log.e("ERROR", "Error reading braille_to_text", e);
        }
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
                        auxTextView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                        auxTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                        auxTextView.setTranslationY(0);

                        if (textBox0.size() > 0) {
                            transactionBox0.add((Float) imageAnimator0.getAnimatedValue());
                        } else {
                            imageAnimator0.setCurrentPlayTime(0);
                            transactionBox0.add(0f);
                        }

                        textBox0.add(auxTextView);
                        frameBox[0].addView(auxTextView);
                    }
                });
            }
        }
    }

}
