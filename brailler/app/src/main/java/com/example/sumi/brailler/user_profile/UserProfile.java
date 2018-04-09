package com.example.sumi.brailler.user_profile;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class UserProfile {

    private final String ALL_HITS_KEY = "AllHits";
    private final String ALL_MISS_KEY = "AllMiss";
    private final String ALL_CONSECUTIVE_HITS_KEY = "AllConsecutiveHits";
    private final String ALL_CONSECUTIVE_MISS_KEY = "AllConsecutiveMiss";

    private SharedPreferences preferences;

    private long allHits, allMiss;
    private long maxConsecutiveHits, maxConsecutiveMiss;

    private int consecutiveHitsCount, consecutiveMissCount;

    private static int correctToUp, wrongToDown;
    private static int progress; //percentage



    public UserProfile(Context mainMenuContext) {
        preferences = mainMenuContext.getSharedPreferences("user", MODE_PRIVATE);

        allHits = preferences.getLong(ALL_HITS_KEY, 0);
        allMiss = preferences.getLong(ALL_MISS_KEY, 0);
        maxConsecutiveHits = preferences.getLong(ALL_CONSECUTIVE_HITS_KEY, 0);
        maxConsecutiveMiss = preferences.getLong(ALL_CONSECUTIVE_MISS_KEY, 0);

        consecutiveHitsCount = 0;
        consecutiveMissCount = 0;

        progress = 0;

    }

    public int getMaxLimit(String type){
        if(type.equals("hits")){
            return consecutiveHitsCount;
        }else if(type.equals("misses")){
            return consecutiveMissCount;
        }else{
            return 0;
        }
    }

    public void saveData(){
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(ALL_HITS_KEY, allHits);
        editor.putLong(ALL_MISS_KEY, allMiss);
        editor.putLong(ALL_CONSECUTIVE_HITS_KEY, maxConsecutiveHits);
        editor.putLong(ALL_CONSECUTIVE_MISS_KEY, maxConsecutiveMiss);
        editor.apply();
    }

    public void addMiss() {
        allMiss += 1;
        consecutiveHitsCount = 0;

        consecutiveMissCount += 1;

        if (consecutiveMissCount > maxConsecutiveMiss){
            maxConsecutiveMiss = consecutiveMissCount;
        }
    }

    public void addHit() {
        allHits += 1;
        consecutiveMissCount = 0;

        consecutiveHitsCount += 1;

        if (consecutiveHitsCount > maxConsecutiveHits){
            maxConsecutiveHits = consecutiveHitsCount;
        }
    }

    public void upDateLevelLimits(){
        if(progress < 21){
            correctToUp = wrongToDown = 5;
            return;
        }else if(progress < 41){
            correctToUp = 5;
            wrongToDown = 4;
            return;
        }else if(progress < 61){
            correctToUp = 5;
            wrongToDown = 3;
            return;
        }else if(progress < 81){
            correctToUp = 5;
            wrongToDown = 2;
            return;
        }else{
            correctToUp = 5;
            wrongToDown = 1;
            return;
        }
    }

    public String getTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        format = "date : "+ format;
        return format;
    }
}
