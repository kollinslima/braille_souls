package com.example.sumi.brailler.tutorial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.example.sumi.brailler.MainMenu;
import com.example.sumi.brailler.R;
import com.example.sumi.brailler.game.LearnGame;

public class LearnGameTutorial extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_tutorial);

        ((Button) findViewById(R.id.skipTutorial)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTutorial();
            }
        });

        final Button finishButton = (Button) findViewById(R.id.finishTutorial);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTutorial();
            }
        });

        final ViewPager viewPager =  (ViewPager) findViewById(R.id.viewPagerLearnTutorial);
        final LearnTutorialAdapter adapter = new LearnTutorialAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == adapter.getCount()-1){
                    finishButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void endTutorial(){
        Intent intent = new Intent(this, LearnGame.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainMenu.user.setTutorialLearnDone();
        MainMenu.user.saveData();
    }
}
