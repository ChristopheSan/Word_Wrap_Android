package com.example.wordwrapgame;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wordwrapgame.logic.Boggle;
import com.example.wordwrapgame.logic.Die;

import java.util.ArrayList;

public class GameboardFragment extends Fragment {
    GameboardFragment gameboardFragment;
    WordlistFragment wordlistFragment;

    Boggle boggle;
    GridLayout gameboardLayout;

    ArrayList<String> solvedWords;
    ArrayList<String> wordList;

    ArrayList<Pair<Integer, Integer>> selectedPath;

    public GameboardFragment() {
        // Required empty public constructor
    }

    public static GameboardFragment newInstance(String param1, String param2) {
        GameboardFragment fragment = new GameboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String[][] testboard = {
                {"a", "B", "C", "D"},
                {"d", "e", "G", "H"},
                {"u", "J", "K", "L"},
                {"l", "N", "O", "P"}
        };

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.d("GameboardFragment", "onCreate: arguments not null");
        }
        boggle = new Boggle(requireContext());
        //boggle.setBoard(testboard);
        boggle.solveBoard();

        solvedWords = boggle.getWordsFound();
//        for (String word : solvedWords) {
//            Log.d("GameboardFragment", "onCreate: " + word);
//        }

        wordList = new ArrayList<>();
        selectedPath = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gameboard, container, false);

        // Initialize UI components

        // Gameboard
        gameboardLayout = view.findViewById(R.id.board_grid);
        initGameboard();

        // Grid TouchListener3
        gameboardLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handleActionDown(motionEvent);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        handleActionMove(motionEvent);
                        return true;
                    case MotionEvent.ACTION_UP:
                        handleActionUp();
                        selectedPath.clear();
                        return true;
                }
                return false;
            }

        });

        return view;
    }

    private void handleActionDown(MotionEvent motionEvent) {
        // Reset selectedPath and highlight the first touched cell
        selectedPath.clear();
        highlightCell(motionEvent);
    }

    private void handleActionMove(MotionEvent motionEvent) {
        // Continuously highlight cells as the user drags their finger
        highlightCell(motionEvent);
    }

    private void handleActionUp() {
        // Check the word formed and reset highlights
        String word = getWordFromPath();
        if (solvedWords.contains(word) && !wordList.contains(word)) {
            Log.d("GameboardFragment", "Added Word: " + word);
            wordList.add(word);
        } else {
            Log.d("GameboardFragment", "Invalid Word: " + word);
        }
        resetHighlights();
        selectedPath.clear();
    }

    private String getWordFromPath() {
        StringBuilder word = new StringBuilder();
        // search through path and get the word
        for (Pair<Integer, Integer> position : selectedPath) {
            int row = position.first;
            int col = position.second;
            word.append(boggle.returnValueAtBoardPos(row, col));
        }
        return word.toString();
    }

    private void resetHighlights() {
        for (int i = 0; i < gameboardLayout.getChildCount(); i++) {
            CardView cardView = (CardView) gameboardLayout.getChildAt(i);
            cardView.getChildAt(0).setBackgroundColor(Color.rgb(254, 169, 169)); // Reset to default color
        }
    }

    private void highlightCell(MotionEvent motionEvent) {
        for (int i = 0; i < gameboardLayout.getChildCount(); i++) {
            CardView cardView = (CardView) gameboardLayout.getChildAt(i);

            // Get the bounds of the CardView
            Rect rect = new Rect();
            cardView.getGlobalVisibleRect(rect);

            // Check if the touch event is inside the CardView
            if (rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
//                Log.d("GameboardFragment", "highlightCell: " + i  + "\n" +
//                        "rect width: " + rect.width() + "\n" +
//                        "rect height: " + rect.height());
                int row = i / boggle.getBoardSize();
                int col = i % boggle.getBoardSize();

                // check if the new pair is adjacent to the last pair in the path
                if (!selectedPath.isEmpty()) {
                    if (!isAdjacentToLastPair(row, col))
                        break;
                }
                Pair<Integer, Integer> position = new Pair<>(row, col);

                if (!selectedPath.contains(position)) {
                    selectedPath.add(position);
                    //cardView.setCardBackgroundColor(Color.rgb(255, 110, 97)); // Highlight the card
                    cardView.getChildAt(0).setBackgroundColor(Color.rgb(255, 110, 97));
                }
                break;
            }
        }
    }

    private boolean isAdjacentToLastPair(int row, int col) {
        Pair<Integer, Integer> lastPair = selectedPath.get(selectedPath.size() - 1);
        int lastRow = lastPair.first;
        int lastCol = lastPair.second;

        int deltaRow = Math.abs(row - lastRow);
        int deltaCol = Math.abs(col - lastCol);

        return (deltaRow <= 1 && deltaCol <= 1);
    }


    private void initGameboard() {
        int boardSize = boggle.getBoardSize();
        Die[][] diceBoard = boggle.getDiceBoard();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++)  {
                CardView cardView = new CardView(requireContext());
                cardView.setCardElevation(8);
                cardView.setRadius(24);
                cardView.setLayoutParams(getCardLayoutParams(i, j));

                TextView textView = new TextView(requireContext());
                textView.setText(diceBoard[i][j].getShowingSide().toUpperCase().charAt(0) + "");
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 64);
                textView.setPadding(18, 18, 18, 18);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setBackgroundColor(Color.rgb(254, 169, 169));
                textView.setTextColor(Color.WHITE);
                //textView.setElevation(12);

                cardView.addView(textView);

                // Add TextView to GridLayout
                gameboardLayout.addView(cardView);
            }
        }
    }

    private GridLayout.LayoutParams getCardLayoutParams(int row, int col) {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(row, 1f); // Equal row weight
        params.columnSpec = GridLayout.spec(col, 1f); // Equal column weight
        params.setMargins(16, 16, 16, 16); // Uniform spacing
        return params;
    }

    private int getDrawableForLetter(char letter) {
        String resourceName = String.valueOf(letter).toLowerCase(); // Ensure lowercase
        return getResources().getIdentifier(resourceName, "drawable", getActivity().getPackageName());
    }

}