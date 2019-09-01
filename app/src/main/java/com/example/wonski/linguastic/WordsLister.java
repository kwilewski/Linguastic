package com.example.wonski.linguastic;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class WordsLister extends Activity {

    private ImageButton prevB, ppB, nextB, homeB, settB, sfB;
    private TextView spTV, enTV;
    private WordManager mWM;
    private long startTime = 0,millis;
    private int min=1,max=100,line,id,seconds,secSet=5;
    private boolean running = false;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            millis = System.currentTimeMillis() - startTime;
            seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            if(seconds>secSet-1){
                String lineS = (String) mWM.getRandomLine();
                if(lineS != null) {
                    diviningString(lineS);
                    timerHandler.removeCallbacks(timerRunnable);
                    startTime = System.currentTimeMillis();
                }
                else{
                    timerHandler.removeCallbacks(timerRunnable);
                }
            }

            timerHandler.postDelayed(this, 50);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_lister);

        spTV = (TextView) findViewById(R.id.spTV);
        enTV = (TextView) findViewById(R.id.enTV);
        prevB = (ImageButton) findViewById(R.id.prevB);
        ppB = (ImageButton) findViewById(R.id.ppB);
        nextB = (ImageButton) findViewById(R.id.nextB);
        homeB = (ImageButton) findViewById(R.id.homeB);
        sfB = (ImageButton) findViewById(R.id.startFloatingB);
        settB = (ImageButton) findViewById(R.id.settingsB);

        getWindow().setStatusBarColor(0x66ff0000);

        mWM = (WordManager) getIntent().getSerializableExtra("list");
        int posit = (Integer) getIntent().getSerializableExtra("position");
        if (savedInstanceState != null){
            mWM = (WordManager) savedInstanceState.getSerializable("list");
            posit = (Integer) savedInstanceState.getSerializable("position");

        }
        mWM.setCurrentPosition(posit);

        mWM.setMax(mWM.getSize());

        String lineS = (String) mWM.getRandomLine();
        diviningString(lineS);

        ppB.setImageResource(R.drawable.ic_play);
        prevB.setImageResource(R.drawable.ic_backward);
        nextB.setImageResource(R.drawable.ic_forward);



        ppB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(running == false) {
                    running = true;
                    ppB.setImageResource(R.drawable.ic_pause);
                    //String lineS = (String) mWM.getRandomLine();
                    //diviningString(lineS);
                    timerHandler.removeCallbacks(timerRunnable);
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                }
                else{
                    running = false;
                    timerHandler.removeCallbacks(timerRunnable);
                    ppB.setImageResource(R.drawable.ic_play);
                }

            }
        });

        nextB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timerHandler.removeCallbacks(timerRunnable);
                String lineS = (String) mWM.getRandomLine();
                if(lineS != null) {
                    diviningString(lineS);
                }
                else{
                    //ArrayList<String> conv = mWM.getWordArray();
                    //mWM = new WordManager(conv);
                }
                seconds = 0;
                if(running == true){
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                }

            }
        });

        prevB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timerHandler.removeCallbacks(timerRunnable);
                String lineS = (String) mWM.getPrevLine();
                if(lineS != null) {
                    diviningString(lineS);
                }
                seconds = 0;
                if(running == true){
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                }

            }
        });

        homeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goHome();

            }
        });

        sfB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initializeView();

            }
        });




    }


    private void goHome(){
        Intent intent = new Intent(WordsLister.this, MainActivity.class);
        startActivity(intent);
    }


    private void initializeView() {

        int currP = mWM.getCurrentPosition();
        Intent intent = new Intent(WordsLister.this, FloatingViewService.class);
        intent.putExtra("position", currP);
        intent.putExtra("list", mWM);
        this.startService(intent);
        finish();
        this.moveTaskToBack(true);

    }



    private void diviningString(String input){
        String[] parts = input.split("\\|");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        spTV.setText(part1);
        enTV.setText(part2);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putSerializable("position", mWM.getCurrentPosition());
        outState.putSerializable("list", mWM);

    }










}
