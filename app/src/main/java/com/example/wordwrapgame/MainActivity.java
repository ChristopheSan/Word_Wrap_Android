package com.example.wordwrapgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button startGameButton;
    private Button statsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGameButton = findViewById(R.id.startGameButton);
        statsButton = findViewById(R.id.gameStatsButton);

        startGameButton.setOnClickListener(v -> {
            // Start the game activity
            startActivity(new Intent(MainActivity.this, GameActivity.class));
        });

        statsButton.setOnClickListener(v -> {
            // Start the stats activity
            startActivity(new Intent(MainActivity.this, StatsActivity.class));
        });

    }
}