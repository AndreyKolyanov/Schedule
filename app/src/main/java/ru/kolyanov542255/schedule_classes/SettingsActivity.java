package ru.kolyanov542255.schedule_classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SettingsActivity extends Activity {

    public static final String DURATION = "duration";

    private Button okButton;
    private Button aboutButton;
    private EditText durationText;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        okButton = (Button) findViewById(R.id.okButton);
        aboutButton = (Button)findViewById(R.id.about);

        durationText = (EditText) findViewById(R.id.durationText);

        mSharedPreferences = getSharedPreferences(WeekPagerActivity.APP_PREFS, Context.MODE_PRIVATE);

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(i);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                mEditor.putInt(DURATION, Integer.parseInt(durationText.getText().toString()));
                mEditor.apply();
            }
        });
    }

 }