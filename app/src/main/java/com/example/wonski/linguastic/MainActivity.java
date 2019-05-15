package com.example.wonski.linguastic;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {



    private TextView textViewEng,textViewSpan,textViewID;
    private int min=1,max=100,line,id,seconds,secSet=5;
    private Button settB,stB;
    private ImageButton infoB;
    private String spanishWord,englishWord,idStr;
    private long startTime = 0,millis;
    private Random rand;
    private WordManager mWordManager;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            millis = System.currentTimeMillis() - startTime;
            seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            if(seconds>secSet-1){
                String lineS = (String) mWordManager.getRandomLine();
                diviningString(lineS);
                timerHandler.removeCallbacks(timerRunnable);
                startTime = System.currentTimeMillis();
            }

            timerHandler.postDelayed(this, 50);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        stB = (Button) findViewById(R.id.startB);
        settB = (Button) findViewById(R.id.settingB);
        infoB = (ImageButton) findViewById(R.id.infoB);

        infoB.setImageResource(R.drawable.ic_info);
        getWindow().setStatusBarColor(0xaaff0000);

        //mWordManager = new WordManager(getArray());




        stB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                /*startService(new Intent(MainActivity.this,FloatingWindow.class));
                onBackPressed();*/
                timerHandler.removeCallbacks(timerRunnable);
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
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);

    }


    public void openLS(){
        Intent intent = new Intent(this,ListSelection.class);
        startActivity(intent);
    }

    private void openSett(){

    }

    private void openInfo(){
        Intent intent = new Intent(this,AppInfo.class);
        startActivity(intent);
    }

    private ArrayList<String> getArrayList(InputStream inputStream){
        ArrayList<String> temp = new ArrayList<String>();
        try {
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String newLine = null;
                while ((newLine = bufferedReader.readLine()) != null ) {
                    temp.add(newLine);
                }
                inputStream.close();
            }
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }

       return temp;
    }


    private void arrayToString(int line){

        ArrayList<String> tempArray = new ArrayList<String>();
        String retString = new String();

        try{
            tempArray = getArrayList(this.getAssets().open("1st100.txt"));
            retString = (String)tempArray.get(line-1);
            //textViewAsset.setText(retString);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        diviningString(retString);
    }

    public ArrayList<String> getArray(){
        ArrayList<String> tempArray = new ArrayList<String>();

        try{
            tempArray = getArrayList(this.getAssets().open("1st100.txt"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return tempArray;
    }


    private void diviningString(String input){
        String[] parts = input.split("\\|");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        textViewEng.setText(part2);
        textViewSpan.setText(part1);
        textViewID.setText(part3);
    }




   /* protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK)
                //If permission granted start floating widget service
//                startFloatingWidgetService();
            else
                //Permission is not available then display toast
//                Toast.makeText(this, getResources().getString(R.string.draw_other_app_permission_denied),Toast.LENGTH_SHORT).show();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/







}
