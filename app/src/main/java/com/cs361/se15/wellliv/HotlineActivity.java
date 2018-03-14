package com.cs361.se15.wellliv;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import android.widget.Toast;
import android.net.Uri;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class HotlineActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ListView infoPhoneList;
    private ActionMenuView amvMenu;
    List<String> list_array = new ArrayList<String>();
    Intent symptom_intent;
    Intent home_intent;
    Intent settings_intent;

    private static final String TAG = "HotlineActivity";

    void call(String phoneNum){
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phoneNum));
        startActivity(dialIntent);
    }

    void setListHotlines(){
        list_array.clear();
        list_array.add("National Sexual Assault Hotline");
        list_array.add("Substance Abuse and Mental Health Services Administration");
        list_array.add("Veteran Crisis Line");
        list_array.add("National Domestic Violence Hotline");
        list_array.add("PTSD Hotline");
        list_array.add("National Suicide Prevention Hotline");
//        mTextMessage.setText(R.string.title_hotlines);
        infoPhoneList = (ListView) findViewById(R.id.hot_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list_array);
        infoPhoneList.setAdapter(arrayAdapter);
        /*This is where setOnItemClickListener for calling is set*/

        infoPhoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: number: " + list_array.get(i));

                //Phone numbers
                String number1 = "18006564673";
                String number2 = "18006624357";
                String number3 = "18002738255";
                String number4 = "18007997233";
                String number5 = "8777177873";
                String number6 = "18002738255";

                //get item clicked from list
                String number = list_array.get(i);

                switch(number) {
                    case "National Sexual Assault Hotline": call(number1);
                        break;
                    case "Substance Abuse and Mental Health Services Administration": call(number2);
                        break;
                    case "Veteran Crisis Line": call(number3);
                        break;
                    case "National Domestic Violence Hotline": call(number4);
                        break;
                    case "PTSD Hotline": call(number5);
                        break;
                    case "National Suicide Prevention Hotline": call(number6);
                        break;
                }

//                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
//                dialIntent.setData(Uri.parse("tel:5038518074"));
//                startActivity(dialIntent);
            }
        });
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
            case R.id.action_home:
                startActivity(home_intent);
                break;
            case R.id.action_settings:
                startActivity(settings_intent);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotline);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle("Hotlines");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.getOverflowIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
        setSupportActionBar(toolbar);

        symptom_intent = new Intent(this, SymptomLogActivity.class);
        home_intent = new Intent(this, MainActivity.class);
        settings_intent = new Intent(this, SettingsActivity.class);
        setListHotlines();
    }

}
