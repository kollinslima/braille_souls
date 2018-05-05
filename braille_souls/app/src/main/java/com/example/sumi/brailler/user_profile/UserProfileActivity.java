package com.example.sumi.brailler.user_profile;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sumi.brailler.MainMenu;
import com.example.sumi.brailler.R;



public class UserProfileActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView progressValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        TextView userName = findViewById(R.id.playerName);//Not showing
        progressValue = findViewById(R.id.user_profile_progress_value);
        progressValue.setText(" " + MainMenu.user.getProgress().toString() + "%");
        userName.setText(getEmiailID(getApplicationContext()));
        progressBar = findViewById(R.id.progressBarProfile);
        progressBar.setProgress(MainMenu.user.getProgress().intValue());

    }

    private String getEmiailID(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }
}
