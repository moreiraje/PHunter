package com.aviGames.treasure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

public class EndGamePopUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game_pop_up);
        DisplayMetrics dm  = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bundle bundle = getIntent().getExtras();
        int bonus = bundle.getInt("bonus");
        int total = bundle.getInt("total");
        String fact = bundle.getString("fact");

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout(width,height);
        TextView bonusText = (TextView) findViewById(R.id.goldBonusAmount);
        TextView totalText = (TextView) findViewById(R.id.totalGoldAmount);
        bonusText.setText(bonus+"");
        totalText.setText(total+"");
        TextView funFact = (TextView) findViewById(R.id.fun_fact_text);
        funFact.setText(fact);


    }
    public void backPress(View view)
    {
        onBackPressed();
    }
}
