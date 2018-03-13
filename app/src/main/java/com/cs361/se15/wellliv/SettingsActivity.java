package com.cs361.se15.wellliv;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SettingsActivity extends AppCompatActivity {

    Intent symptom_intent;
    Intent home_intent;
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

    public String parseJSON(String filename){
        String ret = " ";
        try {
            JSONObject jsonMain = new JSONObject(loadJSON(filename));
            ret = jsonMain.getString("passcode");
        } catch (JSONException e) {
            return "failed parse";
        }
        return ret;
    }

    public Boolean writeJSON(String code){
        BufferedWriter bufferedWriter = null;
        File codeFile = new File(getExternalFilesDir(null), "passcode.json");
        Boolean written = true;
        JSONObject jsonMain;
        try {
            jsonMain = new JSONObject(loadJSON("passcode.json"));
            FileWriter fileWriter = new FileWriter(codeFile);
            bufferedWriter = new BufferedWriter(fileWriter);
            jsonMain.put("passcode", code);
            bufferedWriter.write(jsonMain.toString());
        } catch (Exception e){
            Log.e("Put JSON failed", " sad");
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return written;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        symptom_intent = new Intent(this, SymptomLogActivity.class);
        home_intent = new Intent(this, MainActivity.class);
        settings_intent = new Intent(this, SettingsActivity.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.getOverflowIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        setSupportActionBar(toolbar);

        TextView changeCode = (TextView) findViewById(R.id.change_passcode);
        changeCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String code = parseJSON("passcode.json");
                final EditText oldCode = (EditText) findViewById(R.id.old_passcode);
                final EditText newCode = (EditText) findViewById(R.id.new_passcode);
                newCode.setHint(R.string.prompt_new_password);
                oldCode.setHint(R.string.prompt_old_password);
                oldCode.setVisibility(View.VISIBLE);
                oldCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                        if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                            String oldCodeInput = oldCode.getText().toString();
                            if(oldCodeInput.equals(code)){
                                newCode.setVisibility(View.VISIBLE);
                                newCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                                        if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                                            String newCodeInput = newCode.getText().toString();
                                            writeJSON(newCodeInput);
                                            startActivity(settings_intent);
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                            }
                            return true;
                        }
                        return false;
                    }
                });
                String oldCodeInput = oldCode.getText().toString();
                if(oldCodeInput.equals(code)){
                    newCode.setVisibility(View.VISIBLE);
                    newCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                                return true;
                            }
                            return false;
                        }
                    });
                }
            }
        });
    }
}
