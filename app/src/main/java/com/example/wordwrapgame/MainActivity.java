package com.example.wordwrapgame;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button startGameButton;
    private Button statsButton;

    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.main);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TR_BL,
                new int[]{Color.rgb(98, 160, 254), Color.rgb(145, 168, 250)}
        );
        mainLayout.setBackground(gradientDrawable);


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