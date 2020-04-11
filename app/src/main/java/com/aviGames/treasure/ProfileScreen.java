package com.aviGames.treasure;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        String username = pref.getString("savedName", "Pirate");
       int chestFound = pref.getInt("statsChests", 0);
       int coinFound = pref.getInt("statsCoins", 0);

        EditText name = (EditText)findViewById(R.id.username);
        TextView chest = (TextView) findViewById(R.id.chestStats);
        TextView gold = (TextView) findViewById(R.id.goldStats);

        name.setText(username);
        chest.setText(chestFound+"");
        gold.setText(coinFound+"");
    }
public void backpress(View view){
        onBackPressed();
}
public void changeName(View view){
    EditText name = (EditText)findViewById(R.id.username);
    name.setCursorVisible(true);

}
    @Override
    public void onBackPressed() {
        EditText name = (EditText)findViewById(R.id.username);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("savedName", name.getText().toString());
        editor.apply();
        finish();
    }
}
