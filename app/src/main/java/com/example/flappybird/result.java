package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;

public class result extends AppCompatActivity {

    /** tryAgain function starts handler thread class again, when button is clicked
     * @param GameView **/
    public void tryAgain(android.view.View GameView)
    {

        startActivity(new Intent(getApplicationContext(), HandlerThread.class));

    }


    /** References XML file activity_result when created
     * @param savedInstanceState **/
    @Override
    protected void onCreate(Bundle savedInstanceState) { // after death send player to death screen
        super.onCreate(savedInstanceState);
        // activity_result is the xml file for the death screen
        setContentView(R.layout.activity_result);


    }
}
