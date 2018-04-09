package com.example.sumi.brailler.user_profile;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sumi.brailler.MainMenu;

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

    public UserProfile(Context mainMenuContext) {
        preferences = mainMenuContext.getSharedPreferences("user", MODE_PRIVATE);

        allHits = preferences.getLong(ALL_HITS_KEY, 0);
        allMiss = preferences.getLong(ALL_MISS_KEY, 0);
        maxConsecutiveHits = preferences.getLong(ALL_CONSECUTIVE_HITS_KEY, 0);
        maxConsecutiveMiss = preferences.getLong(ALL_CONSECUTIVE_MISS_KEY, 0);

        consecutiveHitsCount = 0;
        consecutiveMissCount = 0;
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
}
