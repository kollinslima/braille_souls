package com.example.sumi.brailler.tutorial;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.Button;

import com.example.sumi.brailler.tutorial.learn_fragments_tutorial.ExplainFragment;
import com.example.sumi.brailler.tutorial.learn_fragments_tutorial.GamePlay;
import com.example.sumi.brailler.tutorial.learn_fragments_tutorial.Progress;

public class LearnTutorialAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public LearnTutorialAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ExplainFragment();
        } else if (position == 1){
            return new GamePlay();
        } else {
            return new Progress();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

}
