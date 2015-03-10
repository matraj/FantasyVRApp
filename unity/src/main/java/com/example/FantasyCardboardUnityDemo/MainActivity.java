package com.example.FantasyCardboardUnityDemo;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.plugins.unity.UnityCardboardActivity;

//import java.util.logging.Handler;

//import android.support.v7.app.ActionBarActivity;
//import android.view.Menu;
//import android.view.MenuItem;


public class MainActivity extends Activity implements OnClickListener {

    private Button startButton;
    private  final int delayTime = 10000;
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Test Tag" ,"Started main activity");
        startButton = (Button)findViewById(R.id.start_Button);
    }

//    public void onUserInteraction() {
//        myHandler.removeCallbacks(closeControls);
//        myHandler.postDelayed(closeControls, delayTime);
//    }

    private Runnable closeControls = new Runnable() {
        public void run() {
            Toast.makeText(getApplicationContext(), "3 secoonds has gone by buddy!",
                    Toast.LENGTH_LONG).show();
            UnityCardboardActivity.getActivity().onBackPressed();
            Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
            MainActivity.this.startActivity(mainIntent);
        }
    };

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
        /*Toast.makeText(getApplicationContext(), "Hopefully starting up Unity!",
                Toast.LENGTH_SHORT).show();
*/
        Toast.makeText(getApplicationContext(), "Initializing thread",
                Toast.LENGTH_SHORT).show();
//        new backgroundThread().execute();
//        Thread xyz = new Thread(new PhotoDecodeRunnable());
//        Toast.makeText(getApplicationContext(), "Starting thread",
//                Toast.LENGTH_SHORT).show();
//        xyz.start()

        myHandler.postDelayed(closeControls, delayTime);
//        Intent myIntent = new Intent(MainActivity.this, UnityPlayerNativeActivity.class);
        Intent unityIntent = new Intent(MainActivity.this, UnityCardboardActivity.class);
        MainActivity.this.startActivity(unityIntent);
//        Intent intent = new Intent(main.this, .class);
//        startActivity(intent);
//        finish();
//        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.example.FantasyCardboardUnityDemo");
//        startActivity(LaunchIntent);
//        try {
//            wait(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        finish();
    }

//    private class backgroundThread extends AsyncTask {
//
//
//        protected void onProgressUpdate(Integer... progress) {
//
//        }
//
//        protected void onPostExecute(Long result) {
//
//        }
//
//        @Override
//        protected Object doInBackground(Object[] params) {
//            try {
//                Toast.makeText(getApplicationContext(), "Getting into wait",
//                        Toast.LENGTH_SHORT).show();
////                Intent unityIntent = new Intent(MainActivity.this, UnityCardboardActivity.class);
////                MainActivity.this.startActivity(unityIntent);
//                wait(5000);
//                Toast.makeText(getApplicationContext(), "wait is over .. try to close activity",
//                        Toast.LENGTH_SHORT).show();
//                UnityCardboardActivity.getActivity().finish();
//            } catch (InterruptedException e) {
//                Toast.makeText(getApplicationContext(), "Exception happened: " + e.getMessage(),
//                        Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
//
//    public class PhotoDecodeRunnable implements Runnable {
//        @Override
//        public void run() {
//        /*
//         * Code you want to run on the thread goes here
//         */
//            try {
//                Toast.makeText(getApplicationContext(), "Getting into wait",
//                        Toast.LENGTH_SHORT).show();
////                Intent unityIntent = new Intent(MainActivity.this, UnityCardboardActivity.class);
////                MainActivity.this.startActivity(unityIntent);
//                wait(5000);
//                Toast.makeText(getApplicationContext(), "wait is over .. try to close activity",
//                        Toast.LENGTH_SHORT).show();
//                UnityCardboardActivity.getActivity().finish();
//            } catch (InterruptedException e) {
//                Toast.makeText(getApplicationContext(), "Exception happened: " + e.getMessage(),
//                        Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        }
//    }

}

