package com.example.sumi.brailler.game;

import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sumi.brailler.MainMenu;
import com.example.sumi.brailler.R;
import com.example.sumi.brailler.fragments.PauseFragment;

import java.util.Random;
import java.util.Set;

public class LearnModeGame extends AppCompatActivity {


    private final long VIBRATE_TIME = 500; //ms
    private final long ANSWER_INTERVAL = 500; //ms

    public static final String GAME_TAG_LOG = "gameLog";

    private String randomSymbol;

    private TextView symbolDisplay;

    private ToggleButton[] brailleKeyboard = new ToggleButton[6];

    private Button pauseButton;

    private TextView textHits, textMiss;
    private int hitCount, missCount, consecutiveHits, consecutiveMiss;

    private boolean backButtonFlag;

    private Random random;

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_mode_game);
        random = new Random();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        backButtonFlag = false;

        brailleKeyboard[0] = (ToggleButton) findViewById(R.id.braille_button_1x1);
        brailleKeyboard[1] = (ToggleButton) findViewById(R.id.braille_button_1x2);
        brailleKeyboard[2] = (ToggleButton) findViewById(R.id.braille_button_2x1);
        brailleKeyboard[3] = (ToggleButton) findViewById(R.id.braille_button_2x2);
        brailleKeyboard[4] = (ToggleButton) findViewById(R.id.braille_button_3x1);
        brailleKeyboard[5] = (ToggleButton) findViewById(R.id.braille_button_3x2);

        consecutiveMiss = 0;
        consecutiveHits = 0;
        hitCount = 0;
        missCount = 0;

        textHits = (TextView) findViewById(R.id.hit_count);
        textHits.setText(String.valueOf(hitCount));
        textMiss = (TextView) findViewById(R.id.miss_count);
        textMiss.setText(String.valueOf(missCount));

        symbolDisplay = (TextView) findViewById(R.id.symbolDisplay);

        newSymbol();
    }

    public void onPauseClick(View view) {
        pauseGame();
    }
    private void pauseGame() {

        new PauseFragment().showDialog(getSupportFragmentManager());
    }
    private void newSymbol() {

        randomSymbol = getSymbol();
        symbolDisplay.setText(randomSymbol);

        showHint();
    }
    private String getSymbol() {
        String randomKey = null;

        Set<String> keys = MainMenu.text_to_braille.keySet();
        int keyNumber = random.nextInt(keys.size());

        randomKey = (String) keys.toArray()[keyNumber];

        return randomKey;
    }
    private void showHint() {
        String brailleSymbol = MainMenu.text_to_braille.get(randomSymbol);
        int index = 0;

        for (ToggleButton button : brailleKeyboard) {
            button.setBackgroundResource(R.drawable.braille_keyboard_normal_style);

            if (brailleSymbol.charAt(index) == '1') {
                button.setBackgroundResource(R.drawable.braille_keyboard_hint_style);
            }
            index += 1;
        }
    }

    public void onCheckClick(View view) {
        checkAnswer();
    }
    private void checkAnswer() {
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
            if (MainMenu.braille_to_text.get(answer).contains(randomSymbol)) {
                hitCount += 1;
                consecutiveHits += 1;
                consecutiveMiss = 0;
                textHits.setText(String.valueOf(hitCount));

                MainMenu.user.addHit();
            } else {
                consecutiveHits = 0;
                consecutiveMiss += 1;
                missCount += 1;
                textMiss.setText(String.valueOf(missCount));
                MainMenu.user.addMiss();
            }
        } catch (NullPointerException e) {
            consecutiveHits = 0;
            missCount += 1;
            consecutiveMiss += 1;
            textMiss.setText(String.valueOf(missCount));
            MainMenu.user.addMiss();
        }

        showRightAnswer();

    }
    private void showRightAnswer(){

        String brailleSymbol = MainMenu.text_to_braille.get(randomSymbol);
        int index = 0;

        int background = R.drawable.braille_keyboard_right_answer;
        if (consecutiveHits == 0){
            background = R.drawable.braille_keyboard_wrong_answer;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_TIME,VibrationEffect.DEFAULT_AMPLITUDE));
            }else{
                vibrator.vibrate(500);
            }
        }

        for (ToggleButton button : brailleKeyboard) {
            if (brailleSymbol.charAt(index) == '1') {
                button.setBackgroundResource(background);
            }
            index += 1;
        }

        new CountDownTimer(ANSWER_INTERVAL, ANSWER_INTERVAL)
        {
            @Override
            public final void onTick(final long millisUntilFinished){}
            @Override
            public final void onFinish()
            {
                newSymbol();
            }
        }.start();
    }
}
