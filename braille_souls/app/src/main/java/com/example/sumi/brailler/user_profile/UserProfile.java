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

package com.example.sumi.brailler.user_profile;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.StrictMath.toIntExact;

public class UserProfile {

    private final String ALL_HITS_KEY = "AllHits";
    private final String ALL_MISS_KEY = "AllMiss";
    private final String SINGLE_MODE_RECORD = "SingleModeRecord";
    private final String ALL_CONSECUTIVE_HITS_KEY = "AllConsecutiveHits";
    private final String ALL_CONSECUTIVE_MISS_KEY = "AllConsecutiveMiss";
    private final String ALL_USER_PROGRESS = "LearnProgress";

    private final String TUTORIAL_LEARN = "TutorialLearn";
    private final String TUTORIAL_SINGLEPLAYER = "TutorialSinglePlayer";
    private final String TUTORIAL_MULTIPLAYER = "TutorialMultiPlayer";

    private SharedPreferences preferences;

    private long allHits, allMiss, progress;
    private long maxConsecutiveHits, maxConsecutiveMiss;
    private int singleModeRecord, minToHide;
    private boolean tutorialLearn, tutorialSinglePlayer,tutorialMultiPlayer;

    private int consecutiveHitsCount, consecutiveMissCount;
    private HashMap<String, Integer> proficiencyMap;
    private Random random;
    private int lvlHolder;
    private boolean[] wasOnlvl;

    public UserProfile(Context mainMenuContext) {
        preferences = mainMenuContext.getSharedPreferences("user", MODE_PRIVATE);

        allHits = preferences.getLong(ALL_HITS_KEY, 0);
        allMiss = preferences.getLong(ALL_MISS_KEY, 0);
        singleModeRecord = preferences.getInt(SINGLE_MODE_RECORD, 0);
        maxConsecutiveHits = preferences.getLong(ALL_CONSECUTIVE_HITS_KEY, 0);
        maxConsecutiveMiss = preferences.getLong(ALL_CONSECUTIVE_MISS_KEY, 0);
        progress = preferences.getLong(ALL_USER_PROGRESS, 0);

        tutorialLearn = preferences.getBoolean(TUTORIAL_LEARN, true);
        tutorialSinglePlayer = preferences.getBoolean(TUTORIAL_SINGLEPLAYER, true);
        tutorialMultiPlayer = preferences.getBoolean(TUTORIAL_MULTIPLAYER, true);

        consecutiveHitsCount = 0;
        consecutiveMissCount = 0;
        
        proficiencyMap = new HashMap<String, Integer>();
        lvlHolder = 1;
        wasOnlvl = new boolean[6];
        wasOnlvl[1] = wasOnlvl[2] = wasOnlvl[3] = wasOnlvl[4] = wasOnlvl[5] =false;
    }

    public void saveData(){
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(ALL_HITS_KEY, allHits);
        editor.putLong(ALL_MISS_KEY, allMiss);
        editor.putInt(SINGLE_MODE_RECORD, singleModeRecord);
        editor.putLong(ALL_CONSECUTIVE_HITS_KEY, maxConsecutiveHits);
        editor.putLong(ALL_CONSECUTIVE_MISS_KEY, maxConsecutiveMiss);
        editor.putLong(ALL_USER_PROGRESS, progress); //progress to measure difficulty

        editor.putBoolean(TUTORIAL_LEARN, tutorialLearn);
        editor.putBoolean(TUTORIAL_SINGLEPLAYER, tutorialSinglePlayer);
        editor.putBoolean(TUTORIAL_MULTIPLAYER, tutorialMultiPlayer);

        editor.apply();
    }

    public boolean isFirstTimeLearnMode(){
        return tutorialLearn;
    }

    public void setTutorialLearnDone(){
        tutorialLearn = false;
    }

    public void addMiss() {
        allMiss += 1;
        consecutiveHitsCount = 0;

        consecutiveMissCount += 1;

        if (consecutiveMissCount > maxConsecutiveMiss){
            maxConsecutiveMiss = consecutiveMissCount;
        }
        checkProgress();
    }

    public void addHit() {
        allHits += 1;
        consecutiveMissCount = 0;

        consecutiveHitsCount += 1;

        if (consecutiveHitsCount > maxConsecutiveHits){
            maxConsecutiveHits = consecutiveHitsCount;
        }
        checkProgress();
    }

    public void addHitToProficiencyMap(String input){
        if(proficiencyMap.containsKey(input)){
            proficiencyMap.put(input, proficiencyMap.get(input) + 1);
        }else{
            proficiencyMap.put(input, 1);
        }
        checkProficiencyMapLimits(input);
    }

    public void addMissToProficiencyMap(String input){
        if(proficiencyMap.containsKey(input)){
            proficiencyMap.put(input, proficiencyMap.get(input) - 1);
        }else{
            proficiencyMap.put(input, -1);
        }
        checkProficiencyMapLimits(input);
    }

    private void checkProficiencyMapLimits(String input) {
        if(proficiencyMap.get(input) > 50){
            for(HashMap.Entry<String, Integer> it : proficiencyMap.entrySet()){
                if(it.getValue() > 0){
                    proficiencyMap.put(it.getKey(), it.getValue() - 1);
                }else if(it.getValue() < 0){
                    proficiencyMap.put(it.getKey(), it.getValue() + 1);
                }
            }
        }else if(proficiencyMap.get(input) < -50){
            for(HashMap.Entry<String, Integer> it : proficiencyMap.entrySet()){
                if(it.getValue() > 0){
                    proficiencyMap.put(it.getKey(), it.getValue() - 1);
                }else if(it.getValue() < 0){
                    proficiencyMap.put(it.getKey(), it.getValue() + 1);
                }
            }
        }
//        for(HashMap.Entry<String, Integer> it : proficiencyMap.entrySet()){
//            if(it.getValue() > 50){
//
//            }else if(it.getValue() < -50){
//
//            }
//        }
    }

    public boolean checkProgress(){
        if(progress < 21){
            if (consecutiveMissCount > 4){
                getLesserProgress( 1);
                return false;
            }
            if (consecutiveHitsCount > 0) {
                getMoreProgress(1);
                return true;
            }
        }else if (progress < 41){
            if (consecutiveMissCount > 3){
                getLesserProgress( 2);
                return false;
            }
            if (consecutiveHitsCount > 1){
                getMoreProgress(2);
                return true;
            }
        }else if (progress < 61){
            if (consecutiveMissCount > 2){
                getLesserProgress( 3);
                return false;
            }
            if (consecutiveHitsCount > 2) {
                getMoreProgress(3);
                return true;
            }
        }else if (progress < 81){
            if (consecutiveMissCount > 1) {
                getLesserProgress( 4);
                return false;
            }
            if (consecutiveHitsCount > 3) {
                getMoreProgress(4);
                return true;
            }
        }else if (progress < 101){
            if (consecutiveMissCount > 0){
                getLesserProgress( 5);
                return false;
            }
            if (consecutiveHitsCount > 4) {
                getMoreProgress(5);
                return true;
            }
        }else if (progress > 100){
            progress = 100;
            return true;
        }
        return false;
    }

    private void getMoreProgress(int level) {
        if(wasOnlvl[level]){
            if (lvlHolder == 0 ){
                progress++;
                wasOnlvl[1] = wasOnlvl[2] = wasOnlvl[3] = wasOnlvl[4] = wasOnlvl[5] =false;
            }else {
                lvlHolder--;
            }
        }else{
            wasOnlvl[level] = true;
            lvlHolder = level - 1;
        }
    }

    private void getLesserProgress(int level) {
        if(wasOnlvl[level]){
            if (lvlHolder == 0 ){
                progress--;
                wasOnlvl[1] = wasOnlvl[2] = wasOnlvl[3] = wasOnlvl[4] = wasOnlvl[5] =false;
            }else {
                lvlHolder--;
            }
        }else{
            wasOnlvl[level] = true;
            lvlHolder = level - 1;
        }

    }

    public Long getProgress(){
        return progress;
    }

    public ArrayList<String> getWorseFromWorst(){
        ArrayList<String> out = new ArrayList<String>();
        for(HashMap.Entry<String, Integer> it : proficiencyMap.entrySet()){
            if(it.getValue() < 0){
                for(int i = 0; i > it.getValue(); i--){
                    out.add(it.getKey());
                }
            }
        }
        return out;
    }

    public String getRandomWorseFromWorst(){
        ArrayList<String> aux = getWorseFromWorst();
        return aux.get(random.nextInt(aux.size()));
    }

    public ArrayList<String> getWorseFromAll(){
        ArrayList<String> out = new ArrayList<String>();
        for(HashMap.Entry<String, Integer> it : proficiencyMap.entrySet()){
            int temp = it.getValue() + 50;
            for(int i = 0; i < temp; i ++){
                out.add(it.getKey());
            }
        }
        return out;
    }

    public Integer getProficiency(String symbol){
        if(proficiencyMap.containsKey(symbol)){
            return proficiencyMap.get(symbol);
        }
        return 0;
    }

    public String getRandomWorseFromAll() {
        ArrayList<String> aux = getWorseFromAll();
        return aux.get(random.nextInt(aux.size()));
    }

    public int getSingleModeRecord(){
        return singleModeRecord;
    }

    public void setSingleModeRecord(int newRecord){
        singleModeRecord = newRecord;
    }

    
}
