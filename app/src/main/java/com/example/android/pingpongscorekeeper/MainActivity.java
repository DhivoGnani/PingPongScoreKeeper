package com.example.android.pingpongscorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.pingpongscorekeeper.components.PingPongGame;

public class MainActivity extends AppCompatActivity
{
    PingPongGame pingPongGame;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pingPongGame = new PingPongGame();
    }


}
