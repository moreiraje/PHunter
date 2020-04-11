package com.aviGames.treasure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MyHunts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_hunts);
    }
    public void backPress(View view)
    {
        onBackPressed();
    }
}
