package com.example.sumi.brailler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sumi.brailler.fragments.GameOverFragment;
import com.example.sumi.brailler.fragments.PauseFragment;

import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainGame extends AppCompatActivity implements PauseFragment.onDismissListener, GameOverFragment.onDismissListener {


    private final int CHANGE_LEVEL_HITS = 10;
    private final int CHANGE_LEVEL_MISS = 3;

    public static final String GAME_TAG_LOG = "gameLog";

    //10 seconds
//    private final double TIMER_SET = 10;
//    private final int TIMER_INTERVAL = (int) ((TIMER_SET / 60) * 1000);   //ms
//
//    private Timer timer;
//    private int graphicClockMinutes = 0;

    private String randomSymbol;

    private TextView symbolDisplay;

    private ToggleButton[] brailleKeyboard = new ToggleButton[6];

    private Button pauseButton;

    private TextView textHits, textMiss;
    private int hitCount, missCount, consecutiveHits, consecutiveMiss;

    private boolean backButtonFlag;

    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        random = new Random();

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

    @Override
    protected void onPause() {
        super.onPause();
        if (!backButtonFlag) {
            pauseGame();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backButtonFlag = true;
    }

    private void newSymbol() {

        randomSymbol = getSymbol();
        symbolDisplay.setText(randomSymbol);

        showAnswer();

        gameContinue();
    }

    private void showAnswer() {
        String brailleSymbol = MainMenu.text_to_braille.get(randomSymbol);
        int index = 0;

        for (ToggleButton button : brailleKeyboard) {
            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.braille_keyboard_normal));

            if (brailleSymbol.charAt(index) == '1') {
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.braille_keyboard_with_hint));
            }
            index += 1;
        }
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
            } else {
                consecutiveHits = 0;
                consecutiveMiss += 1;
                missCount += 1;
                textMiss.setText(String.valueOf(missCount));
            }
        } catch (NullPointerException e) {
            consecutiveHits = 0;
            missCount += 1;
            consecutiveMiss += 1;
            textMiss.setText(String.valueOf(missCount));
        }

        newSymbol();

    }

    private String getSymbol() {
        String randomKey = null;

        Set<String> keys = MainMenu.text_to_braille.keySet();
        int keyNumber = random.nextInt(keys.size());

        randomKey = (String) keys.toArray()[keyNumber];

        return randomKey;
    }

    public void onPauseClick(View view) {
        pauseGame();
    }

    private void pauseGame() {

        new PauseFragment().showDialog(getSupportFragmentManager());
    }

    private void gameContinue() {

    }

    @Override
    public void continuarJogoFragment() {

        newSymbol();
    }

    @Override
    public void resetGame() {

        consecutiveMiss = 0;
        consecutiveHits = 0;
        hitCount = 0;
        missCount = 0;

        textHits.setText(String.valueOf(hitCount));
        textMiss.setText(String.valueOf(missCount));

        newSymbol();
    }

    @Override
    public void backToMainMenu() {
        finish();
    }

    public void onCheckClick(View view) {
        checkAnswer();
    }

//    public class TempoJogo extends TimerTask {
//
//        @Override
//        public void run() {
//
//            graphicClockMinutes = (graphicClockMinutes + 1) % 60;
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    graphicClock.setMinute(graphicClockMinutes);
//                }
//            });
//
//            if (graphicClockMinutes == 0) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        checkAnswer();
//                    }
//                });
//            }
//        }
//    }

}
