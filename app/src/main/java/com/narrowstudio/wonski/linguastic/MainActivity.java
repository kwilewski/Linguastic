package com.narrowstudio.wonski.linguastic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity {


    private Button settB,stB;
    private ImageButton infoB;
    private boolean stateDarkModeSwitch, stateDoubleTimeSwitch;
    private SharedPreferences preferences;
    private int stateTime;

    private static final String SHARED_PREFS = "PREFS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
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

        setContentView(R.layout.activity_main);

        stB = (Button) findViewById(R.id.startB);
        settB = (Button) findViewById(R.id.settingB);
        infoB = (ImageButton) findViewById(R.id.infoB);

        infoB.setImageResource(R.drawable.ic_info);

        stB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                /*startService(new Intent(MainActivity.this,FloatingWindow.class));
                onBackPressed();*/
                openLS();
            }
        });

        settB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSett();

            }
        });

        infoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openInfo();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.moveTaskToBack(true);
    }


    public void openLS(){
        Intent intent = new Intent(this,ListSelection.class);
        startActivity(intent);
    }

    private void openSett(){
        Intent intent = new Intent(this,MySettings.class);
        startActivity(intent);

    }

    private void openInfo(){
        Intent intent = new Intent(this,AppInfo.class);
        startActivity(intent);
    }








}
