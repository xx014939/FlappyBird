package com.example.flappybird;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private boolean hasDied = false; // Used to help re-set game loop when user has died
    private GameView gameView; // initialise GameView class object
    private Handler handler = new Handler(); // handler object used for handler thread
    private final static long TIMER_INTERVAL = 30; // used to define delay time


    /** Create entire game **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // run game

        gameView = new GameView(this);
        setContentView(gameView);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() { // Running the Gameview class in handler
                    @Override
                    public void run () {
                        gameView.invalidate();

                        if (GameView.isDead == true && hasDied == false ) // if user dies
                        {

                            startActivity(new Intent(MainActivity.this, result.class)); // show results screen

                            hasDied = true; // set this boolean to true for two reasons. 1. allows the user to use "try again" button multiple times
                            // 2. shows result screen once, instead of re-printing it continually while the if loop conditions are met
                        }

                    }
                });

            }
        }, 0, TIMER_INTERVAL);

    }
}
