package com.example.wordwrapgame;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


public class StatsActivity extends AppCompatActivity {
    private ConstraintLayout mainLayout;

    private TextView totalScore;
    private TextView totalGames;
    private TextView totalCompletions;
    private TextView totalWords;
    private TextView averageWordsPerGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_activity);
        // Initialize UI components
        mainLayout = findViewById(R.id.main);

        totalScore = findViewById(R.id.total_score_value);
        totalGames = findViewById(R.id.total_games_value);
        totalCompletions = findViewById(R.id.total_completions_value);
        totalWords = findViewById(R.id.total_words_value);
        averageWordsPerGame = findViewById(R.id.average_words_per_game_value);

        populateStats();

        // Create the gradient
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TR_BL,
                new int[]{Color.rgb(255, 110, 97), Color.rgb(254, 169, 169)}
        );

        mainLayout.setBackground(gradientDrawable);
    }

    private void populateStats() {
        // Grab shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("stats", MODE_PRIVATE);

        float totalScoreflt = sharedPreferences.getFloat("total_score", 0);
        int totalGamesInt = sharedPreferences.getInt("total_games_played", 0);
        int totalCompletionsInt = sharedPreferences.getInt("total_games_completed", 0);
        int totalWordsInt = sharedPreferences.getInt("total_words_found", 0);
        float averageWordsPerGameFlt = sharedPreferences.getFloat("average_words_per_game", 0);


        totalScore.setText(String.valueOf(totalScoreflt));
        totalGames.setText(String.valueOf(totalGamesInt));
        totalCompletions.setText(String.valueOf(totalCompletionsInt));
        totalWords.setText(String.valueOf(totalWordsInt));
        averageWordsPerGame.setText(String.valueOf(averageWordsPerGameFlt));

    }
}