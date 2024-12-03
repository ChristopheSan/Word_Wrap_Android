package com.example.wordwrapgame;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity {

    private GameboardFragment gameboardFragment;
    private FrameLayout gameboardFragmentContainer;
    private LinearLayoutCompat mainLayout;

    private GridView gameboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize UI components
        gameboardFragmentContainer = findViewById(R.id.gameboard_fragment_container);
        mainLayout = (LinearLayoutCompat) findViewById(R.id.main);

        // Gradient for the background
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TR_BL,
                new int[]{Color.rgb(98, 160, 254), Color.rgb(145, 168, 250)}
        );
        mainLayout.setBackground(gradientDrawable);


        // Initialize gameboard fragment
        gameboardFragment = new GameboardFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.gameboard_fragment_container, gameboardFragment)
                .commit();
    }
}