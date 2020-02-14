package com.example.treasure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartUpScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_screen);



    }



    public void continueHunt(View view){
        Intent startHunt = new Intent(this, MapActivity.class);
        startActivity(startHunt);
    }


    public void newHunt(View view){
        Intent newHunt = new Intent(this, NewGame.class);
        startActivity(newHunt);
    }
    public void myHunts(View view){
        Intent myHunts = new Intent(this,MyHunts.class);
        startActivity(myHunts);
    }

}
