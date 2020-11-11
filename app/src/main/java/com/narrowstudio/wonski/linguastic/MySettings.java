package com.narrowstudio.wonski.linguastic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.annotation.Nullable;

public class MySettings extends Activity {

    private Switch darkModeSwitch, doubleTimeSwitch;
    private SeekBar timeSB;
    private boolean stateDarkModeSwitch, stateDoubleTimeSwitch;
    private SharedPreferences preferences;
    private int stateTime;

    private static final String SHARED_PREFS = "PREFS";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    protected void onResume(){
        super.onResume();
        createUI();
    }

    private void createUI(){
        preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        stateDarkModeSwitch = preferences.getBoolean("dark_mode", false);
        stateDoubleTimeSwitch = preferences.getBoolean("double_time", false);
        stateTime = preferences.getInt("time", 0);

        if (stateDarkModeSwitch) {
            setTheme(R.style.AppDarkTheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }


        setContentView(R.layout.activity_settings);

        darkModeSwitch = (Switch) findViewById(R.id.switchSett);
        doubleTimeSwitch = (Switch) findViewById(R.id.switch2Sett);
        timeSB = (SeekBar) findViewById(R.id.seekBarSett);

        darkModeSwitch.setChecked(stateDarkModeSwitch);
        doubleTimeSwitch.setChecked(stateDoubleTimeSwitch);
        timeSB.setProgress(stateTime);

        darkModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateDarkModeSwitch = !stateDarkModeSwitch;
                //commented to update after theme change
                //darkModeSwitch.setChecked(stateDarkModeSwitch);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("dark_mode", stateDarkModeSwitch);
                editor.apply();
                Intent intent = new Intent(MySettings.this,MySettings.class);
                startActivity(intent);
                finish();
            }
        });

        doubleTimeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateDoubleTimeSwitch = !stateDoubleTimeSwitch;
                doubleTimeSwitch.setChecked(stateDoubleTimeSwitch);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("double_time", stateDoubleTimeSwitch);
                editor.apply();
            }
        });

        timeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                stateTime = progress;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("time", stateTime);
                editor.apply();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
