package com.example.wordwrapgame;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wordwrapgame.logic.Boggle;
import com.example.wordwrapgame.logic.Die;

public class GameboardFragment extends Fragment {
    GameboardFragment gameboardFragment;
    WordlistFragment wordlistFragment;

    Boggle boggle;
    GridLayout gameboardLayout;

    public GameboardFragment() {
        // Required empty public constructor
    }

    public static GameboardFragment newInstance(String param1, String param2) {
        GameboardFragment fragment = new GameboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.d("GameboardFragment", "onCreate: arguments not null");
        }
        boggle = new Boggle(requireContext());
        boggle.solveBoard();
        boggle.printSolvedWords();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gameboard, container, false);

        // Initialize UI components

        // Gameboard
        gameboardLayout = view.findViewById(R.id.board_grid);
        initGameboard();

        return view;
    }

    private void initGameboard() {
        int boardSize = boggle.getBoardSize();
        Die[][] diceBoard = boggle.getDiceBoard();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++)  {
                CardView textHolder = new CardView(requireContext());

                TextView textView = new TextView(requireContext());
                textView.setText(diceBoard[i][j].getShowingSide().toUpperCase().charAt(0) + "");
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 64);
                textView.setPadding(16, 10, 16, 10);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setBackgroundColor(Color.rgb(254, 169, 169));
                textView.setTextColor(Color.WHITE);
                textView.setElevation(12);

                //gameboardLayout.addView(textView);

                // Set LayoutParams
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(i, 1f); // Equal weight for rows
                params.columnSpec = GridLayout.spec(j, 1f); // Equal weight for columns
                params.setMargins(16, 16, 16, 16); // Add gap between elements
                textView.setLayoutParams(params);

                // Add TextView to GridLayout
                gameboardLayout.addView(textView);
            }
        }
    }

    private int getDrawableForLetter(char letter) {
        String resourceName = String.valueOf(letter).toLowerCase(); // Ensure lowercase
        return getResources().getIdentifier(resourceName, "drawable", getActivity().getPackageName());
    }

}