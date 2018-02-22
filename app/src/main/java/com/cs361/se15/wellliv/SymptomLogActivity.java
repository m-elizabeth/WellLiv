package com.cs361.se15.wellliv;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;

public class SymptomLogActivity extends AppCompatActivity {

    private ActionMenuView amvMenu;
    Intent symptom_intent;
    Intent home_intent;
    Intent add_symptom;

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
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_log);

        symptom_intent = new Intent(this, SymptomLogActivity.class);
        home_intent = new Intent(this, MainActivity.class);
        add_symptom = new Intent(this, AddSymptomActivity.class);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle("Symptom Log");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(add_symptom);
            }
        });
    }

}
