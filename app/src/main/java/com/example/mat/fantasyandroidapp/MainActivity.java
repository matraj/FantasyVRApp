package com.example.mat.fantasyandroidapp;

import android.app.Activity;
//import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener {

    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Test Tag" ,"Started main activity");
        startButton = (Button)findViewById(R.id.start_Button);
    }

    @Override
    public void onClick(View v) {
        Log.d("Test Tag" ,"Something clicked");
        if (v == startButton) {
            Toast.makeText(getApplicationContext(), "Better start up Unity!",
                    Toast.LENGTH_LONG).show();
            Log.d("Test Tag" ,"StartButton clicked");
        }
    }

    public void test(View v){
        Log.d("Test Tag", "Something clicked");
        Toast.makeText(getApplicationContext(), "Trying to start up Unity!",
                Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(main.this, .class);
//        startActivity(intent);
//        finish();
    }
}
