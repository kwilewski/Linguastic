package com.example.wonski.linguastic;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class WordManager implements Serializable {


    private int min=1,max,line,id,seconds,secSet=5;
    private String spanishWord,englishWord,idStr;
    private long startTime = 0,millis;
    private Random rand;
    private ArrayList<String> wordArray;
    private ArrayList<Integer> qID;
    private ArrayList<String> queueArray;
    private int currentEl;





    public WordManager(ArrayList<String> list){
        SetArray(list);
        this.max = wordArray.size();
        qID = new ArrayList<>();
        queueArray = new ArrayList<>();
        createQ();
        this.currentEl = 0;
    }

    public int getSize(){
        int ret = wordArray.size();
        return ret;
    }

    public ArrayList<String> getWordArray(){
        return wordArray;
    }

    public void SetArray(ArrayList<String> list){
        this.wordArray = list;
    }

    public String getRandomLine(){
        if(currentEl < queueArray.size()) {
            currentEl++;
            String retString = queueArray.get(currentEl - 1);
            return retString;
        }
        return null;
    }

    public String getPrevLine(){
        if(currentEl > 1) {
            currentEl--;
            String retString = queueArray.get(currentEl - 1);
            return retString;
        }
        return null;
    }

    public void createQ(){
        int el;
        int newID;
        String retString = null;
        ArrayList<String> randArray = (ArrayList) wordArray.clone();
        for(el = 0; el < wordArray.size(); el++) {
            max = randArray.size();
            int line = newRandom();
            retString = (String) randArray.get(line - 1);
            String[] parts = retString.split("\\|");
            String partID = parts[2];
            newID = Integer.valueOf(partID);
            qID.add(newID);
            queueArray.add(retString);
            randArray.remove(line-1);
        }
    }


    public void setMax(int time){
        this.max = time;
    }




    public int newRandom(){
        rand = new Random(System.nanoTime());
        int rl = (int) (rand.nextDouble()*(max)+1);
        return rl;
    }


    public int getCurrentPosition(){
        int temp = currentEl-1;
        return temp;
    }

    public void setCurrentPosition(int position){
        this.currentEl = position;
    }



    private String getText(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {;
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String newLine = null;
                while ((newLine = bufferedReader.readLine()) !=
                        null ) {
                    stringBuilder.append(newLine+"\n");
                }
                inputStream.close();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
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


    /*private void arrayToString(int line){

        ArrayList<String> tempArray = new ArrayList<String>();
        String retString = new String();

        try{
            tempArray = getArrayList(getAssets().open("1st100.txt"));
            retString = (String) tempArray.get(line-1);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        diviningString(retString);
    }*/

    private void diviningString(String input){
        String[] parts = input.split("\\|");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        //MainActivity.MAChanger(part1, part2, part3);
    }




}
