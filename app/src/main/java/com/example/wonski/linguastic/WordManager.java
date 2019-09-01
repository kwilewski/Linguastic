package com.example.wonski.linguastic;


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
        else{
            currentEl = 1;
            String retString = queueArray.get(currentEl - 1);
            return retString;
        }
    }

    public String getPrevLine(){
        if(currentEl > 1) {
            currentEl--;
            String retString = queueArray.get(currentEl - 1);
            return retString;
        }
        else{
            currentEl = queueArray.size();
            String retString = queueArray.get(currentEl - 1);
            return retString;
        }
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


    public void addElements(){
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





}
