package com.narrowstudio.wonski.linguastic;

import android.app.Activity;
import android.content.SharedPreferences;


public class ColorHelper extends Activity {

    private boolean stateDarkModeSwitch, stateDoubleTimeSwitch;
    private SharedPreferences preferences;
    private int stateTime;
    private static final String SHARED_PREFS = "PREFS";

    public ColorHelper(){
        setPrefs();
    }

    public void setPrefs(){
        preferences = getSharedPreferences(SHARED_PREFS, 0);
        stateDarkModeSwitch = preferences.getBoolean("dark_mode", false);
        stateDoubleTimeSwitch = preferences.getBoolean("double_time", false);
        stateTime = preferences.getInt("time", 0);
    }

    public int getColorBackground2(){
        if (stateDarkModeSwitch){
            return R.color.colorBackgroundDark2;
        }
        else {
            return R.color.colorBackground2;
        }
    }

    public int getColorChecked(){
        if (stateDarkModeSwitch){
            return R.color.colorCheckedDark;
        }
        else {
            return R.color.colorChecked;
        }
    }
}
