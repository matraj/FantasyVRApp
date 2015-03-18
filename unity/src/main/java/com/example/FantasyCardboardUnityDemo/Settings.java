package com.example.FantasyCardboardUnityDemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by knowroaming on 15-03-17.
 */
public class Settings extends Activity{

    public boolean shouldRecord;

    private ToggleButton audioToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        audioToggle = (ToggleButton)findViewById(R.id.audioToggle);
    }

    public void easyModeClicked(View view) {
        Toast.makeText(getApplicationContext(), "So,... I see you chose the easy eh!",
                Toast.LENGTH_LONG).show();
    }

    public void shouldRecord(View view) {
        if (audioToggle.isChecked()){
            ((MyApplication) this.getApplication()).setShouldRecord(true);
        } else {
            ((MyApplication) this.getApplication()).setShouldRecord(false);
        }
    }
}
