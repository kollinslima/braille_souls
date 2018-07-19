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

package com.braille.souls.braille_souls.tutorial;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.Button;

import com.braille.souls.braille_souls.tutorial.learn_fragments_tutorial.ExplainFragment;
import com.braille.souls.braille_souls.tutorial.learn_fragments_tutorial.GamePlay;
import com.braille.souls.braille_souls.tutorial.learn_fragments_tutorial.Progress;

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
