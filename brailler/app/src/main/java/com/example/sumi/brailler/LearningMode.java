package com.example.sumi.brailler;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sumi.brailler.fragments.GameOverFragment;
import com.example.sumi.brailler.fragments.PauseFragment;

import java.util.Random;
import java.util.Set;

public class LearningMode extends AppCompatActivity implements PauseFragment.onDismissListener, GameOverFragment.onDismissListener {

    private final int CHANGE_LEVEL_HITS = 10;
    private final int CHANGE_LEVEL_MISS = 3;

    private final long VIBRATE_TIME = 500; //ms
    private final long ANSWER_INTERVAL = 500; //ms

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
        setContentView(R.layout.activity_learning_mode);
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
    public void continueGameFragment() {
        newSymbol();
    }

    @Override
    public void backToMainMenu() {
        finish();
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
//            button.setBackgroundDrawable(getResources().getDrawable(R.drawable.braille_keyboard_normal_style));

            if (brailleSymbol.charAt(index) == '1') {
//                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.braille_keyboard_hint_style));
                button.setBackgroundResource(R.drawable.braille_keyboard_hint_style);
            }
            index += 1;
        }
    }
}
