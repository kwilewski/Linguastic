package com.example.wonski.linguastic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ListSelection extends Activity implements MyAdapter.OnItemListener {

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    private TextView textView;
    private Button sBut, sfwBut;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> listsArray;
    private ArrayList<String> namesArray = new ArrayList<>();
    private ArrayList<Integer> selectedArray = new ArrayList<>();
    private ArrayList<String> selWords = new ArrayList<>();
    private WordManager mWMLS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_selection);



        textView = (TextView) findViewById(R.id.textView);
        mRecyclerView = (RecyclerView) findViewById(R.id.listRV);
        sBut = (Button) findViewById(R.id.startButton);
        sfwBut = (Button) findViewById(R.id.startAFWButton);


        getWindow().setStatusBarColor(0xaaff0000);


        //mRecyclerView.setHasFixedSize(true);


        getLists();


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(listsArray, selectedArray, this);
        mRecyclerView.setAdapter(mAdapter);





        sBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cleanSleected();
                int sum=0, sc;
                for(sc=0; sc<listsArray.size(); sc++){
                    if(selectedArray.get(sc) == 1) {
                        sum++;
                    }
                }
                if(sum != 0) {
                    selWords = setArray();
                    mWMLS = new WordManager(selWords);

                    Intent intent = new Intent(ListSelection.this, WordsLister.class);
                    intent.putExtra("list", mWMLS);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ListSelection.this, "Select at leats one list of words",Toast.LENGTH_SHORT).show();
                }






            }
        });

        sfwBut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                cleanSleected();


                //Check if the application has draw over other apps permission or not?
                //This permission is by default available for API<23. But for API > 23
                //you have to ask for the permission in runtime.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(ListSelection.this)) {


                    //If the draw over permission is not available open the settings screen
                    //to grant the permission.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                } else {

                    cleanSleected();
                    int sum = 0, sc;
                    for (sc = 0; sc < listsArray.size(); sc++) {
                        if (selectedArray.get(sc) == 1) {
                            sum++;
                        }
                    }
                    if (sum != 0) {
                        selWords = setArray();
                        mWMLS = new WordManager(selWords);
                        initializeView();

                    } else {
                        Toast.makeText(ListSelection.this, "Select at leats one list of words", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });




    }


    /**
     * Set and initialize the view elements.
     */
    private void initializeView() {

                Intent intent = new Intent(ListSelection.this, FloatingViewService.class);
                intent.putExtra("position", 0);
                intent.putExtra("list", mWMLS);
                this.startService(intent);
                //finish();
        this.moveTaskToBack(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //-------------------------------------------------------------------------------------------------------------------------------------------


    public WordManager getWM(){
        return mWMLS;
    }

    public ArrayList<String> getSelNames(){
        return this.selWords;
    }

    private void cleanSleected(){
        if(selectedArray.get(0)==1){
            int q;
            for(q=1; q<=listsArray.size()-1; q++){
                selectedArray.set(q,0);
               // mAdapter.notifyDataSetChanged();
            }
        }

        if(selectedArray.get(11)==1){
            int q;
            for(q=1; q<=10; q++){
                selectedArray.set(q,0);
                //mAdapter.notifyDataSetChanged();
            }
        }
    }

    private ArrayList<String> setArray(){

        ArrayList<String> tempArray = new ArrayList<>();
        ArrayList<String> retArray = new ArrayList<>();
        int temp;
        for(temp = 0;temp < listsArray.size();temp++) {
            if(selectedArray.get(temp) == 1) {
                String fname = namesArray.get(temp);
                try {
                    tempArray = getSelectedWords(this.getAssets().open(fname));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int t2;
                for(t2 = 0; t2 < tempArray.size(); t2++){
                    retArray.add(tempArray.get(t2));

                }
                tempArray.clear();
            }
        }
        return retArray;
    }


    private ArrayList<String> getSelectedWords(InputStream inputStream){
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



    private ArrayList<String> getArrayList(InputStream inputStream){
        ArrayList<String> temp = new ArrayList<String>();
        try {
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String newLine = null;
                while ((newLine = bufferedReader.readLine()) != null ) {
                    String[] parts = newLine.split("\\|");
                    //String part1 = "\"" + parts[0] +"\"";
                    String part1 = parts[0];
                    String part2 = parts[1];
                    temp.add(part2);
                    namesArray.add(part1);
                }
                inputStream.close();
            }
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }


        return temp;
    }


    private void getLists(){
        try{
            listsArray = getArrayList(this.getAssets().open("list.txt"));
        }
        catch (IOException e){
            e.printStackTrace();
        }

        int temp = listsArray.size();

        for(int i=0;i<=temp;i++){
            selectedArray.add(0);
        }


    }


    @Override
    public void onItemClick(int position) {

        if(selectedArray.get(position)==0){
            selectedArray.set(position, 1);
        }
        else{
            selectedArray.set(position, 0);
        }

        if(position==0){
            if(selectedArray.get(position)==1){
                int temp;
                for(temp=1;temp<=listsArray.size()-1;temp++){
                    selectedArray.set(temp,1);
                }
            }
            else{
                int temp;
                for(temp=1;temp<=listsArray.size()-1;temp++){
                    selectedArray.set(temp,0);
                }
            }
        }

        if(position==11){
            if(selectedArray.get(position)==1){
                int temp;
                for(temp=1;temp<=10;temp++){
                    selectedArray.set(temp,1);
                }
            }
            else{
                int temp;
                for(temp=0;temp<=10;temp++){
                    selectedArray.set(temp,0);
                }
            }
        }

        if(position != 0 && selectedArray.get(0) == 1){
            selectedArray.set(0,0);
        }

        if(position > 0 && position < 11 && selectedArray.get(11) == 1){
            selectedArray.set(11,0);
        }

        mAdapter.notifyDataSetChanged();
    }
}
