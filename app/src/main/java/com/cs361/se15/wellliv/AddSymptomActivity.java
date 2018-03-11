package com.cs361.se15.wellliv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.JsonWriter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import android.text.format.DateFormat;

public class AddSymptomActivity extends AppCompatActivity {

    Intent symptom_intent;
    Intent home_intent;
    Intent settings_intent;
    private static final String NAME = "NAME";
    private SimpleExpandableListAdapter mAdapter;
    ExpandableListView simpleExpandableListView;
    String [] symptomsLogged = new String[15];
    int logCount = 0;
    private String groupItems[] = {"Physical", "Mental", "Mood"};
    private String[][] childItems =
            {{"Tired", "Low Appetite", "Insomnia", "Headache", "Pain"},
                    {"Anxious", "Irritable", "Feeling Isolated", "Nightmares", "Suicidal Thoughts"},
                    {"Happy", "Calm", "Angry", "Guilty", "Sad"}};

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_symptoms:
                startActivity(symptom_intent);
                break;
            case R.id.action_home:
                startActivity(home_intent);
                break;
            case R.id.action_settings:
                startActivity(settings_intent);
                break;
        }
        return true;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public static String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public String loadJSON(String filename){
        String json = null;
        File JSONfile = new File(getExternalFilesDir(null), filename);
        try {
            json = getStringFromFile(JSONfile.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    protected JSONObject writeObj(String date, String [] syms, int num){
        JSONObject newObj = new JSONObject();
        try {
            newObj.put("date", date);
            JSONArray newArr = new JSONArray();
            for(int i = 0; i < num; i++){
                newArr.put(syms[i]);
            }
            newObj.put("symptoms", newArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newObj;
    }

    protected void logSymptoms(String date, String [] list, int num){
        // parse existing/init new JSON
        OutputStream out = null;
        BufferedWriter bufferedWriter = null;
        JSONObject mainObj;
        JSONArray mainArr;
        File outFile = new File(getExternalFilesDir(null), "symptoms.json");
        try {
            mainObj = new JSONObject(loadJSON("symptoms.json"));
            mainArr = mainObj.getJSONArray("logs");
            FileWriter fileWriter = new FileWriter(outFile);
            bufferedWriter = new BufferedWriter(fileWriter);
            //create object to put in array
            JSONObject newObj = writeObj(date, list, num);
            mainArr.put(newObj);
            bufferedWriter.write(mainObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptom);

        symptom_intent = new Intent(this, SymptomLogActivity.class);
        home_intent = new Intent(this, MainActivity.class);
        settings_intent = new Intent(this, SettingsActivity.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle("Add Symptoms");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.getOverflowIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        setSupportActionBar(toolbar);

        simpleExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        final List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();

        //Add list data
        for(int i = 0; i < groupItems.length; i++){
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(NAME, groupItems[i]);
            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for(int j = 0; j < childItems[i].length; j++){
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(NAME, childItems[i][j]);
            }
            childData.add(children);
        }

        // define arrays for displaying data in Expandable list view
        String groupFrom[] = {NAME};
        int groupTo[] = {R.id.textViewParent};
        final String childFrom[] = {NAME};
        int childTo[] = {R.id.textViewChild};

        // Set up the adapter
        mAdapter = new SimpleExpandableListAdapter(this, groupData,
                R.layout.list_parent,
                groupFrom, groupTo,
                childData, R.layout.list_child,
                childFrom, childTo);
        simpleExpandableListView.setAdapter(mAdapter);

        // perform set on group click listener event
        simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(groupPosition));
                if(parent.isItemChecked(index)){
                    parent.setItemChecked(index, false);
                }
                else {
                    parent.setItemChecked(index, true);
                }

                return false;
            }
        });
        // perform set on child click listener event
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                String message;
                TextView tx = (TextView) v;
                message=tx.getText().toString();
                symptomsLogged[logCount] = message;
                logCount++;
                return false;
            }
        });

        Button log_button = (Button) findViewById(R.id.buttonLog);
        log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar date = Calendar.getInstance();
                String month = date.get(Calendar.MONTH)+"";
                int monthInt = (int) Integer.parseInt(month);
                monthInt++;
                int day = date.get(Calendar.DAY_OF_MONTH);
                String year         = (String) DateFormat.format("yyyy", date); // 2013
                String today = monthInt + "/" +day + "/" + year;
                logSymptoms(today, symptomsLogged, logCount);
                Toast.makeText(getApplicationContext(), "Logged", Toast.LENGTH_LONG).show();
                startActivity(symptom_intent);
            }
        });
    }
}
