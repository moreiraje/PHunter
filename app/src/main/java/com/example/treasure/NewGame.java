package com.example.treasure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NewGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
    }
    public void backPress(View view)
    {
        onBackPressed();
    }
}
