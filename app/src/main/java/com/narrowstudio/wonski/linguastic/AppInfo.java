package com.narrowstudio.wonski.linguastic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class AppInfo extends Activity {
    private boolean stateDarkModeSwitch;
    private SharedPreferences preferences;

    private static final String SHARED_PREFS = "PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onResume() {
        super.onResume();
        createUI();
    }

    private void createUI(){
        preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        stateDarkModeSwitch = preferences.getBoolean("dark_mode", false);
        if (stateDarkModeSwitch) {
            setTheme(R.style.AppDarkTheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_app_info);
        TextView gora = (TextView)findViewById(R.id.textView2);
        TextView dol = (TextView)findViewById(R.id.textView4);
        gora.setText(R.string.app_info_1);
        dol.setText(R.string.app_info_2);


    }





}
