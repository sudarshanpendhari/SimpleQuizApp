package com.codedev.demo;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ScoreActivity extends AppCompatActivity {
    private TextView score,correct,incorrect,unanswered;
    private Button done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = findViewById(R.id.sa_score);
        done = findViewById(R.id.sa_done);
        correct = findViewById(R.id.correct);
        incorrect = findViewById(R.id.incorrect);
        unanswered = findViewById(R.id.unanswered);

        String score_str = getIntent().getStringExtra("SCORE");
        String incorrect_str = getIntent().getStringExtra("incorrect");
        String unans_str = getIntent().getStringExtra("unanswered");
        String score1_str = getIntent().getStringExtra("score");

        score.setText(score_str);
        correct.setText(score1_str);
        incorrect.setText(String.valueOf(incorrect_str));
        unanswered.setText(String.valueOf(unans_str));

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(ScoreActivity.this, categoryActivity.class));
                ScoreActivity.this.finish();
            }
        });
    }
    @Override
    public void onBackPressed(){
        ScoreActivity.super.onBackPressed();
        startActivity(new Intent(ScoreActivity.this,categoryActivity.class));


    }
}