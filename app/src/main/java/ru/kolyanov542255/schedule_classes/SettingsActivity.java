package ru.kolyanov542255.schedule_classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SettingsActivity extends Activity {

    public static final String DURATION = "duration";

    private Button okButton;
    private EditText durationText;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        okButton = (Button) findViewById(R.id.okButton);
        durationText = (EditText) findViewById(R.id.durationText);

        mSharedPreferences = getSharedPreferences(WeekPagerActivity.APP_PREFS, Context.MODE_PRIVATE);

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