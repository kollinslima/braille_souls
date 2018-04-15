package com.example.sumi.brailler.game;

import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sumi.brailler.MainMenu;
import com.example.sumi.brailler.R;
import com.example.sumi.brailler.fragments.GameOverFragment;
import com.example.sumi.brailler.fragments.MultiplayerChooseFragment;
import com.example.sumi.brailler.fragments.PauseFragment;
import com.example.sumi.brailler.fragments.PlayerWaitFragment;
import com.example.sumi.brailler.fragments.PointTableMultiplayer;
import com.plusquare.clockview.ClockView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MultiplayerGame extends AppCompatActivity
        implements PauseFragment.onDismissListener, MultiplayerChooseFragment.onDismissListener,
        PlayerWaitFragment.onDismissListener, PointTableMultiplayer.onDismissListener {

    private final long VIBRATE_TIME = 500; //ms
    private final long ANSWER_INTERVAL = 500; //ms

    public static final String GAME_TAG_LOG = "gameLog";

    //60 seconds
//    private final double TIMER_SET = 60;
    private final double TIMER_SET = 5;
    private final int TIMER_INTERVAL = (int) ((TIMER_SET / 60) * 1000);   //ms

    private Timer timer;
    private int graphicClockMinutes = 0;
    private ClockView graphicClock;

    private String randomSymbol;

    private TextView symbolDisplay;

    private ToggleButton[] brailleKeyboard = new ToggleButton[6];

    private Button pauseButton;

    private TextView textHits, textMiss;
    private int hitCount, missCount;

    private boolean backButtonFlag, gameOverFlag, isGameRunning;

    private Random random;

    private Vibrator vibrator;

    private int numberOfPlayers, playerTurn;
    private ArrayList<Integer> playerPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_game);

        random = new Random();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        playerTurn = 1;

        backButtonFlag = false;
        gameOverFlag = false;

        brailleKeyboard[0] = (ToggleButton) findViewById(R.id.braille_button_1x1);
        brailleKeyboard[1] = (ToggleButton) findViewById(R.id.braille_button_1x2);
        brailleKeyboard[2] = (ToggleButton) findViewById(R.id.braille_button_2x1);
        brailleKeyboard[3] = (ToggleButton) findViewById(R.id.braille_button_2x2);
        brailleKeyboard[4] = (ToggleButton) findViewById(R.id.braille_button_3x1);
        brailleKeyboard[5] = (ToggleButton) findViewById(R.id.braille_button_3x2);

        hitCount = 0;
        missCount = 0;

        textHits = (TextView) findViewById(R.id.hit_count);
        textHits.setText(String.valueOf(hitCount));
        textMiss = (TextView) findViewById(R.id.miss_count);
        textMiss.setText(String.valueOf(missCount));

        symbolDisplay = (TextView) findViewById(R.id.symbolDisplay);

        graphicClock = (ClockView) findViewById(R.id.graphicClock);

        isGameRunning = false;
        new MultiplayerChooseFragment().showDialog(getSupportFragmentManager());

    }

    @Override
    protected void onPause() {
        super.onPause();

        if ((!(backButtonFlag || gameOverFlag)) && isGameRunning) {
            pauseGame();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            timer.cancel();
        } catch (NullPointerException e) {
            Log.e("Timer", "Timer not running", e);
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
                textHits.setText(String.valueOf(hitCount));

                MainMenu.user.addHit();
            } else {
                missCount += 1;
                textMiss.setText(String.valueOf(missCount));
                MainMenu.user.addMiss();
            }
        } catch (NullPointerException e) {
            missCount += 1;
            textMiss.setText(String.valueOf(missCount));
            MainMenu.user.addMiss();
        }

        newSymbol();

    }

    private String getSymbol() {
        String randomKey = null;

        do {
            Set<String> keys = MainMenu.text_to_braille.keySet();
            int keyNumber = random.nextInt(keys.size());

            randomKey = (String) keys.toArray()[keyNumber];
        } while (randomKey == null);

        return randomKey;
    }

    public void onPauseClick(View view) {
        pauseGame();
    }

    private void pauseGame() {
        try {
            timer.cancel();
        } catch (NullPointerException e) {
            Log.e("Timer", "Timer not running", e);
        }
        new PauseFragment().showDialog(getSupportFragmentManager());
    }

    @Override
    public void continueGameFragment() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new GameTimer(), TIMER_INTERVAL, TIMER_INTERVAL);
//        newSymbol();
    }

    @Override
    public void resetGame() {
        playerTurn = 1;
        new MultiplayerChooseFragment().showDialog(getSupportFragmentManager());
    }

    @Override
    public void backToMainMenu() {
        gameOverFlag = true;
        finish();
    }

    public void onCheckClick(View view) {
        checkAnswer();
    }

    @Override
    public void startGame(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        playerPoints = new ArrayList<Integer>(numberOfPlayers);
        playerPoints.clear();

        new PlayerWaitFragment().showDialog(getSupportFragmentManager(), playerTurn);
    }

    @Override
    public void startTurn() {

        hitCount = 0;
        missCount = 0;
        textHits.setText(String.valueOf(hitCount));
        textMiss.setText(String.valueOf(missCount));

        for (ToggleButton b : brailleKeyboard) {
            b.setChecked(false);
        }

        isGameRunning = true;
        newSymbol();

        timer = new Timer();
        timer.scheduleAtFixedRate(new GameTimer(), TIMER_INTERVAL, TIMER_INTERVAL);
    }

    public void endPlayerTurn() {
        int totalPoints = hitCount - missCount;
        playerPoints.add(playerTurn - 1, totalPoints);
        playerTurn += 1;

        isGameRunning = false;

        if (playerTurn > numberOfPlayers) {
            new PointTableMultiplayer().showDialog(getSupportFragmentManager(), new ArrayList<Integer>(playerPoints), true);
        } else {
            new PlayerWaitFragment().showDialog(getSupportFragmentManager(), playerTurn);
            new PointTableMultiplayer().showDialog(getSupportFragmentManager(), new ArrayList<Integer>(playerPoints), false);
        }

        try {
            timer.cancel();
        } catch (NullPointerException e) {
            Log.e("Timer", "Timer not running", e);
        }
    }

    public class GameTimer extends TimerTask {

        @Override
        public void run() {

            graphicClockMinutes = (graphicClockMinutes + 1) % 60;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    graphicClock.setMinute(graphicClockMinutes);
                }
            });

            if (graphicClockMinutes == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        endPlayerTurn();
                    }
                });
            }
        }
    }

}
