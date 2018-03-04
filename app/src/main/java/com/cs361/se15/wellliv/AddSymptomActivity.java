package com.cs361.se15.wellliv;

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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSymptomActivity extends AppCompatActivity {

    Intent symptom_intent;
    Intent home_intent;
    Intent settings_intent;
    private static final String NAME = "NAME";
    private SimpleExpandableListAdapter mAdapter;
    ExpandableListView simpleExpandableListView;
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

    public void writeJsonStream(OutputStream out, String date, String [] list) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writer.beginObject();
        writer.name(date);
        writeJsonArray(writer, list);
        writer.endObject();
        writer.close();
    }

    public void writeJsonArray(JsonWriter writer, String [] list){
        try {
            writer.beginObject();
            writer.name("symptoms:");
            writer.beginArray();
            writer.value(list[0]);
            writer.endArray();
            writer.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void logSymptoms(String date, String [] list){
        // parse existing/init new JSON
        OutputStream out = null;
        File outFile = new File(getExternalFilesDir(null), "symptoms.json");
        try {
            out = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            writeJsonStream(out, date, list);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();

        final String [] symptomsLogged = {"sad", "anxious"};
        int logCount = 0;

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

                int cind = parent.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(childPosition));
                //symptomsLogged[0] = childItems[cind][0];
                return false;
            }
        });

        Button log_button = (Button) findViewById(R.id.buttonLog);
        log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logSymptoms("3/3/2018", symptomsLogged);
                Toast.makeText(getApplicationContext(), "Logged", Toast.LENGTH_LONG).show();
                startActivity(symptom_intent);
            }
        });
    }
}
