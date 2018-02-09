package com.cs361.se15.wellliv;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ListView mainPhoneList;
    private ActionMenuView amvMenu;
    List<String> list_array = new ArrayList<String>();
    Intent ptsd_intent;
    Intent abuse_intent;
    Intent dep_intent;
    Intent sa_intent;
    Intent symptom_intent;
    String ptsd = new String("PTSD");
    String abuse = new String("Abuse");
    String assault = new String("Sexual Assault");
    String depression = new String("Depression");

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    setListHome();
                    return true;
                case R.id.navigation_info:
                    mTextMessage.setText(R.string.title_information);
                    setListInfo();
                    return true;
                case R.id.navigation_resource:
                    mTextMessage.setText(R.string.title_resources);
                    setListResource();
                    return true;
            }
            return false;
        }
    };

    void setListHome(){
        list_array.clear();
        list_array.add("Emergency Phone Numbers:");
        list_array.add("911");
        list_array.add("Poison Control");
        list_array.add("Suicide Hotline");
        mainPhoneList = (ListView) findViewById(R.id.main_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list_array);
        mainPhoneList.setAdapter(arrayAdapter);
    }

    void setListInfo(){
        list_array.clear();
        list_array.add("Info Pages:");
        list_array.add("PTSD");
        list_array.add("Sexual Assault");
        list_array.add("Abuse");
        list_array.add("Depression");
        mainPhoneList = (ListView) findViewById(R.id.main_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list_array);
        mainPhoneList.setAdapter(arrayAdapter);

        mainPhoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = list_array.get(i);
                if(ptsd.equals(selectedItem)){
                    startActivity(ptsd_intent);
                }
                if(abuse.equals(selectedItem)){
                    startActivity(abuse_intent);
                }
                if(depression.equals(selectedItem)){
                    startActivity(dep_intent);
                }
                if(assault.equals(selectedItem)){
                    startActivity(sa_intent);
                }
            }
        });
    }

    void setListResource(){
        list_array.clear();
        list_array.add("Ways to get help:");
        list_array.add("Hotlines");
        list_array.add("Psychotherapy");
        list_array.add("Support Groups");
        mainPhoneList = (ListView) findViewById(R.id.main_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list_array);
        mainPhoneList.setAdapter(arrayAdapter);
    }

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
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle("WellLiv");
        setSupportActionBar(toolbar);

        ptsd_intent = new Intent(this, PTSDActivity.class);
        abuse_intent = new Intent(this, AbuseActivity.class);
        dep_intent = new Intent(this, DepressionActivity.class);
        sa_intent = new Intent(this, SexualAssaultActivity.class);
        symptom_intent = new Intent(this, SymptomLogActivity.class);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setListHome();
    }
}
