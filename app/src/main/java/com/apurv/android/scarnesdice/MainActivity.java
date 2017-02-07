package com.apurv.android.scarnesdice;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button roll,hold,reset;
    private Activity activity;
    private ImageView image;
    private TextView status,turn;
    private int playerScore=0,
            computerScore=0,
            turnScore=0;
    private boolean isPlayerTurn = true;
    private int currentDiceValue = 1;
    int images[] = {R.drawable.dice1, R.drawable.dice2,
            R.drawable.dice3, R.drawable.dice4,
            R.drawable.dice5, R.drawable.dice6};
    private String SCOREPLAYER = "Your Score: ";
    private String SCORECOMPUTER = "My Score: ";
    private String SCORETURN = "Turn Score: ";
    private AdView mAdView;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        activity = this;
        activity.findViewById(android.R.id.content).setBackgroundColor(Color.parseColor("#27ae60"));

        roll = (Button)findViewById(R.id.roll);
        hold = (Button)findViewById(R.id.hold);
        reset = (Button)findViewById(R.id.reset);
        image = (ImageView)findViewById(R.id.image);
        status = (TextView)findViewById(R.id.text);
        turn = (TextView)findViewById(R.id.turn);

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlayerTurn)rollDice();
            }
        });

        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlayerTurn)holdDice();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlayerTurn)resetDice();
            }
        });
    }



    private void updateUI(){
        status.setText(SCOREPLAYER + playerScore + "\n"
                + SCORECOMPUTER + computerScore + "\n"
                + SCORETURN + turnScore);
        image.setImageResource(images[currentDiceValue-1]);
        turn.setText(isPlayerTurn?"YOUR TURN!":"MY TURN!");
        if(!isPlayerTurn){
            activity.findViewById(android.R.id.content).setBackgroundColor(Color.parseColor("#8e44ad"));
            roll.setEnabled(false);
            hold.setEnabled(false);
        }
        else{
            activity.findViewById(android.R.id.content).setBackgroundColor(Color.parseColor("#27ae60"));
            roll.setEnabled(true);
            hold.setEnabled(true);
        }
    }

    private void rollDice(){
        currentDiceValue = new Random().nextInt(6) + 1;
        if(currentDiceValue==1){
            turnScore = 0;
            holdDice();
        } else {
            turnScore += currentDiceValue;
        }
        updateUI();
    }

    private void holdDice(){
        if(isPlayerTurn)playerScore += turnScore;
        else computerScore += turnScore;

        turnScore = 0;
        currentDiceValue = 1;
        isPlayerTurn = !isPlayerTurn;
        updateUI();

        if(computerScore>100 || playerScore>100){
            Toast.makeText(this,(computerScore>100?"Computer":"Player")
                    +" won",Toast.LENGTH_SHORT).show();
            resetDice();
        }
        if(!isPlayerTurn){
            new android.os.Handler().postDelayed(new Runnable(){
                @Override
                public void run(){
                    computerTurn();
                }
            },1000);
        }
    }

    private void resetDice(){
        playerScore = turnScore = computerScore = 0;
        isPlayerTurn = true;
        updateUI();
    }

    private void computerTurn() {
        if (!isPlayerTurn) {
            if (turnScore < 20) {
                rollDice();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        computerTurn();
                    }
                },1000);
            } else
                holdDice();
        }
    }

}
