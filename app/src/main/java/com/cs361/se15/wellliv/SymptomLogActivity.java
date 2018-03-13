package com.cs361.se15.wellliv;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.CalendarView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

public class SymptomLogActivity extends AppCompatActivity {

    private ActionMenuView amvMenu;
    Intent symptom_intent;
    Intent home_intent;
    Intent add_symptom;
    Intent settings_intent;

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

    public void createDialog(String date, String list){
        AlertDialog.Builder dialog = new AlertDialog.Builder(SymptomLogActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle(date);
        dialog.setMessage("Symptoms: \n"+list);
        dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Delete".
            }
        })
                .setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    private void copyAssets() {
        AssetManager assetManager = getApplicationContext().getAssets();
        String[] files = null;
        try {files = assetManager.list("");
        } catch (IOException e) {Log.e("tag", "Failed to get asset file list.", e);}
        if (files != null) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open("symptoms.json");
                File outFile = new File(getExternalFilesDir(null), "symptoms.json");
                out = new FileOutputStream(outFile, true);
                copyFile(in, out);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + "symptoms.json", e);
            } finally {
                if (in != null) {
                    try {in.close();} catch (IOException e) {}
                }
                if (out != null) {
                    try {out.close();} catch (IOException e) {}
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public static String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
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

    public String loadJSON(){
        String json = null;
        File JSONfile = new File(getExternalFilesDir(null), "symptoms.json");
        try {
            json = getStringFromFile(JSONfile.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public String parseJSON(String date){
        String ret = " ";
        int i;
        try {
            JSONObject jsonMain = new JSONObject(loadJSON());
            JSONArray arrayMain = jsonMain.getJSONArray("logs");
            JSONArray arraysyms = null;
            for(i = 0; i < arrayMain.length(); i++){
                JSONObject cur = arrayMain.getJSONObject(i);
                if(date.equals(cur.getString("date"))){
                    arraysyms = cur.getJSONArray("symptoms");
                }
            }
            if(arraysyms == null)
                return "No symptoms logged";
            for(i = 0; i < arraysyms.length(); i++){
                if(!(arraysyms.getString(i).equals(""))){
                    ret += arraysyms.getString(i) +" \n ";
                }
            }
        } catch (JSONException e) {
            return "No symptoms logged";
        }
        return ret;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_log);

        //File delete = new File(getExternalFilesDir(null), "symptoms.json");
        //delete.delete();

        File JSONfile = new File(getExternalFilesDir(null), "symptoms.json");
        if(!JSONfile.exists()) {
            copyAssets();
            //Toast.makeText(getApplicationContext(), "copied", Toast.LENGTH_LONG).show();
        }

        symptom_intent = new Intent(this, SymptomLogActivity.class);
        home_intent = new Intent(this, MainActivity.class);
        add_symptom = new Intent(this, AddSymptomActivity.class);
        settings_intent = new Intent(this, SettingsActivity.class);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle("Symptom Log");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.getOverflowIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        setSupportActionBar(toolbar);

        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month++;
                String date = ""+month+"/"+day+"/"+year;
                String symptomsList = parseJSON(date);
                createDialog(""+month+"/"+day+"/"+year, symptomsList);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(add_symptom);
            }
        });
    }

}
