package com.example.wonski.linguastic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WordLister {

    private ArrayList<String> selList;



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


  /*  private void arrayToString(int line){

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
    }*/

    /*private void diviningString(String input){
        String[] parts = input.split("\\|");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
    }



    private ArrayList<String> getSelList(){
        arrayToString();
    }
*/
}
