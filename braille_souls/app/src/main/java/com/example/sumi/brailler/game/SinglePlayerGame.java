package com.example.sumi.brailler.game;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sumi.brailler.MainMenu;
import com.example.sumi.brailler.R;
import com.example.sumi.brailler.fragments.PauseFragment;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class SinglePlayerGame extends AppCompatActivity
        implements PauseFragment.onDismissListener, View.OnClickListener {

    private final int TRACKS = 4;
    private final int HIT_SPEED_UP = 1;
    public static final String GAME_TAG_LOG = "gameLog";

    private int speedDown = 20000; //20s
    private final int INCREASE_SPEED_FACTOR = 1000; //1s
    private int timerInterval = 3000; //3s
    private Timer[] timerAddSymbol = new Timer[TRACKS];

    private String randomSymbol;

    private ToggleButton[] brailleKeyboard = new ToggleButton[6];

    private Button pauseButton, checkButton;
    private ImageView playAgainImage;

    private FrameLayout[] frameBox = new FrameLayout[TRACKS];

    private boolean backButtonFlag, gameOverFlag, isGameRunning, speedUpFlag;

    private Random random;
    private int hitCount, totalHits;
    private TextView hitView, hitRecord;

    private ValueAnimator[] imageAnimator = new ValueAnimator[TRACKS];

    private ArrayList<TextView>[] textBox = new ArrayList[TRACKS];
    private ArrayList<Float>[] transactionBox = new ArrayList[TRACKS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer_game);

        random = new Random();

        backButtonFlag = false;
        gameOverFlag = false;
        totalHits = 0;

        checkButton = (Button) findViewById(R.id.check_button);
        checkButton.setOnClickListener(this);

        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseGame();
            }
        });

        hitRecord = (TextView) findViewById(R.id.record_single_mode);
        hitView = (TextView) findViewById(R.id.hitView);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(hitView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        playAgainImage = (ImageView) findViewById(R.id.playAgainArrow);

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

        isGameRunning = false;

        setUpGame();
    }

    private void setUpGame() {

        checkButton.setText("CHECK");
        playAgainImage.setVisibility(View.INVISIBLE);
        isGameRunning = true;
        gameOverFlag = false;
        speedUpFlag = false;

        hitCount = 0;
        hitView.setText(String.valueOf(totalHits));
        hitRecord.setText("Record: " + MainMenu.user.getSingleModeRecord());

        for (int i = 0; i < TRACKS; i++) {
            textBox[i] = new ArrayList<>();
            transactionBox[i] = new ArrayList<>();
            frameBox[i].removeAllViews();
            timerAddSymbol[i] = new Timer();
        }

        timerAddSymbol[0].scheduleAtFixedRate(new AddNewSymbol0(), timerInterval/2, timerInterval);
        timerAddSymbol[1].scheduleAtFixedRate(new AddNewSymbol1(), timerInterval/2, timerInterval);
        timerAddSymbol[2].scheduleAtFixedRate(new AddNewSymbol2(), timerInterval/2, timerInterval);
        timerAddSymbol[3].scheduleAtFixedRate(new AddNewSymbol3(), timerInterval/2, timerInterval);

        for (int i = 0; i < TRACKS; i++) {

            imageAnimator[i] = ValueAnimator.ofFloat(0.0f, 1.0f);
            imageAnimator[i].setRepeatCount(0);
            imageAnimator[i].setInterpolator(new LinearInterpolator());
            imageAnimator[i].setDuration(speedDown);

            final int finalI = i;
            imageAnimator[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    final float progress = (float) animation.getAnimatedValue();
                    final float heightFrame = frameBox[finalI].getHeight();

                    if (textBox[finalI].size() > 0) {
                        if ((progress - transactionBox[finalI].get(0)) >= 1) {
                            gameOver(finalI);
                        } else {
                            final float baseHeigh = textBox[finalI].get(0).getHeight();

                            if (baseHeigh != 0) {
                                final float efectiveHeigh = heightFrame - baseHeigh;

                                for (int j = 0; j < textBox[finalI].size(); j++) {
                                    textBox[finalI].get(j).setTranslationY(efectiveHeigh * (progress - transactionBox[finalI].get(j)));
                                }
                            }
                        }
                    }
                }
            });

            imageAnimator[i].start();
        }

    }

    private void gameOver(int symbolIndex) {
        gameOverFlag = true;
        checkButton.setText("");
        playAgainImage.setVisibility(View.VISIBLE);

        if (totalHits > MainMenu.user.getSingleModeRecord()){
            MainMenu.user.setSingleModeRecord(totalHits);
        }

        totalHits = 0;

        textBox[symbolIndex].get(0).setTextColor(getResources().getColor(R.color.end_of_line_single_player));

        for (int i = 0; i < TRACKS; i++) {
            imageAnimator[i].cancel();
            timerAddSymbol[i].cancel();
        }
    }

    private String getSymbol() {
        String randomKey = null;

        Set<String> keys = MainMenu.text_to_braille.keySet();
        int keyNumber = random.nextInt(keys.size());

        randomKey = (String) keys.toArray()[keyNumber];

        return randomKey;
    }

    @Override
    protected void onPause() {
        super.onPause();

        MainMenu.user.saveData();

        if ((!(backButtonFlag || gameOverFlag)) && isGameRunning) {
            pauseGame();
        }
    }

    private void pauseGame() {
        try {
            for (int i = 0; i < TRACKS; i++) {
                imageAnimator[i].pause();
                timerAddSymbol[i].cancel();
            }
        } catch (NullPointerException e) {
            Log.e("ERRO", "Timer not running", e);
        }


        new PauseFragment().showDialog(getSupportFragmentManager());
    }

    @Override
    public void continueGameFragment() {

        for (int i = 0; i < TRACKS; i++) {
            imageAnimator[i].resume();
            timerAddSymbol[i] = new Timer();
        }

        timerAddSymbol[0].scheduleAtFixedRate(new AddNewSymbol0(), timerInterval, timerInterval);
        timerAddSymbol[1].scheduleAtFixedRate(new AddNewSymbol1(), timerInterval, timerInterval);
        timerAddSymbol[2].scheduleAtFixedRate(new AddNewSymbol2(), timerInterval, timerInterval);
        timerAddSymbol[3].scheduleAtFixedRate(new AddNewSymbol3(), timerInterval, timerInterval);

    }

    @Override
    public void backToMainMenu() {
        gameOverFlag = true;
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backButtonFlag = true;
    }

//    private Context getContext() {
//        return this;
//    }

    @Override
    public void onClick(View v) {
        if (gameOverFlag) {
            setUpGame();
        } else {
            String answer = "";

            for (ToggleButton button : brailleKeyboard) {
                if (button.isChecked()) {
                    answer = answer.concat("1");
                } else {
                    answer = answer.concat("0");
                }

                button.setChecked(false);
            }

            removeSymbol(answer);
            hitView.setText(String.valueOf(totalHits));
        }
    }

    private void removeSymbol(String answer) {
        int[] location1 = new int[2];
        int[] location2 = new int[2];
        int winnerViewToRemove = -1;
        int[] indexToRemove = new int[TRACKS];
        View[] viewToRemove = new View[]{null, null, null, null};
        boolean[] findFirst = new boolean[]{false, false, false, false};

        try {
            //Find Symbol in columns
            for (int k = 0; k < TRACKS; k++) {
                for (int i = 0; i < textBox[k].size() && !findFirst[k]; i++) {
                    if (MainMenu.braille_to_text.get(answer).contains(textBox[k].get(i).getText().toString())) {
                        viewToRemove[k] = textBox[k].get(i);
                        indexToRemove[k] = i;
                        findFirst[k] = true;
                    }
                }
            }

            for (int i = 0; i < TRACKS; i++) {
                if (viewToRemove[i] != null) {
                    if (winnerViewToRemove == -1) {
                        winnerViewToRemove = i;
                    } else {
                        viewToRemove[winnerViewToRemove].getLocationOnScreen(location1);
                        viewToRemove[i].getLocationOnScreen(location2);

                        if (location2[1] > location1[1]) {
                            winnerViewToRemove = i;
                        }
                    }
                }
            }

            if (winnerViewToRemove >= 0) {

                ((ViewGroup) viewToRemove[winnerViewToRemove].getParent()).removeView(viewToRemove[winnerViewToRemove]);
                textBox[winnerViewToRemove].remove(textBox[winnerViewToRemove].indexOf(viewToRemove[winnerViewToRemove]));
                transactionBox[winnerViewToRemove].remove(indexToRemove[winnerViewToRemove]);

                final float oldAnimationValue = (float) imageAnimator[winnerViewToRemove].getAnimatedValue();
                imageAnimator[winnerViewToRemove].setCurrentPlayTime(0);

                for (int j = 0; j < transactionBox[winnerViewToRemove].size(); j++) {
                    transactionBox[winnerViewToRemove].set(j, -(oldAnimationValue - transactionBox[winnerViewToRemove].get(j)));
                }

                speedUpGame();
            }

        } catch (NullPointerException e) {
            Log.e("ERROR", "Error reading braille_to_text OR Symbol not found", e);
        }
    }

    private void speedUpGame() {

        hitCount += 1;
        totalHits += 1;

        if (!speedUpFlag) {
            if (hitCount >= HIT_SPEED_UP) {
                speedUpFlag = true;
                for (int k = 0; k < TRACKS; k++) {
                    timerAddSymbol[k].cancel(); //No new symbols.
                }
            }
        }

        if (speedUpFlag) {
            boolean allEmpty = true;

            for (int i = 0; i < TRACKS; i++) {
                if (frameBox[i].getChildCount() > 0) {
                    allEmpty = false;
                }
            }

            if (allEmpty) {

                for (int i = 0; i < TRACKS; i++) {
                    imageAnimator[i].cancel();
                }

                double beta = ((double) speedDown)/timerInterval;
                speedDown = speedDown - INCREASE_SPEED_FACTOR;
                timerInterval = (int) (speedDown/beta);
                Toast.makeText(this, "Speed Up!!!", Toast.LENGTH_SHORT).show();
                setUpGame();
            }

        }
    }

    private TextView getTextView() {
        TextView auxTextView = new TextView(this);
        auxTextView.setText(getSymbol());
        auxTextView.setTextColor(Color.BLACK);
        auxTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        auxTextView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        auxTextView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        auxTextView.setTranslationY(0);

        return auxTextView;
    }

    private class AddNewSymbol0 extends TimerTask {

        @Override
        public void run() {

            if (random.nextBoolean()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        TextView auxTextView = getTextView();

                        if (textBox[0].size() > 0) {
                            transactionBox[0].add((Float) imageAnimator[0].getAnimatedValue());
                        } else {
                            imageAnimator[0].setCurrentPlayTime(0);
                            transactionBox[0].add(0f);
                        }

                        textBox[0].add(auxTextView);
                        frameBox[0].addView(auxTextView);
                    }
                });
            }
        }
    }

    private class AddNewSymbol1 extends TimerTask {

        @Override
        public void run() {

            if (random.nextBoolean()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView auxTextView = getTextView();

                        if (textBox[1].size() > 0) {
                            transactionBox[1].add((Float) imageAnimator[1].getAnimatedValue());
                        } else {
                            imageAnimator[1].setCurrentPlayTime(0);
                            transactionBox[1].add(0f);
                        }

                        textBox[1].add(auxTextView);
                        frameBox[1].addView(auxTextView);
                    }
                });
            }
        }
    }

    private class AddNewSymbol2 extends TimerTask {

        @Override
        public void run() {

            if (random.nextBoolean()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView auxTextView = getTextView();

                        if (textBox[2].size() > 0) {
                            transactionBox[2].add((Float) imageAnimator[2].getAnimatedValue());
                        } else {
                            imageAnimator[2].setCurrentPlayTime(0);
                            transactionBox[2].add(0f);
                        }

                        textBox[2].add(auxTextView);
                        frameBox[2].addView(auxTextView);
                    }
                });
            }
        }
    }

    private class AddNewSymbol3 extends TimerTask {

        @Override
        public void run() {

            if (random.nextBoolean()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView auxTextView = getTextView();

                        if (textBox[3].size() > 0) {
                            transactionBox[3].add((Float) imageAnimator[3].getAnimatedValue());
                        } else {
                            imageAnimator[3].setCurrentPlayTime(0);
                            transactionBox[3].add(0f);
                        }

                        textBox[3].add(auxTextView);
                        frameBox[3].addView(auxTextView);
                    }
                });
            }
        }
    }

}
