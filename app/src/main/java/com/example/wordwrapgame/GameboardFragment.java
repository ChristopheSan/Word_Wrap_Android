package com.example.wordwrapgame;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wordwrapgame.logic.Boggle;
import com.example.wordwrapgame.logic.Die;

import java.util.ArrayList;

public class GameboardFragment extends Fragment {

    private static final String[][] testboard = {
            {"a", "B", "C", "D"},
            {"d", "e", "G", "H"},
            {"u", "J", "K", "L"},
            {"l", "N", "O", "P"}
    };
    Boggle boggle;
    GridLayout gameboardLayout;
    FrameLayout overlay;

    ArrayList<String> solvedWords;
    ArrayList<String> wordList;

    ArrayList<Pair<Integer, Integer>> selectedPath;

    int currentWordsFound;
    double currentScore;

    TextView scoreValue;
    TextView wordCountValue;

    CardView showFoundWordsButton;
    CardView showRemainingWordsButton;
    ImageView returnToMenuButton;


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
        // return to menu
        returnToMenuButton = view.findViewById(R.id.return_to_menu_button);
        returnToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });

        // Animation Overlay
        overlay = view.findViewById(R.id.overlay);

        // Score Components
        scoreValue = view.findViewById(R.id.score_value);
        wordCountValue = view.findViewById(R.id.word_count_value);
        currentWordsFound = 0;
        currentScore = 0;
        updateCardAmounts();

        // Buttons
        showFoundWordsButton = view.findViewById(R.id.show_found_words_button);
        showRemainingWordsButton = view.findViewById(R.id.show_remaining_words_button);

        showFoundWordsButton.setClickable(true);
        showRemainingWordsButton.setClickable(true);


        showFoundWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("GameboardFragment", "showFoundWordsButton clicked");
                WordDisplayBottomSheet bottomSheet = new WordDisplayBottomSheet(wordList, true);
                bottomSheet.show(getParentFragmentManager(), "bottomSheet");
            }
        });
        showRemainingWordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndGameConfirmationDialog();
            }
        });


        // Game board
        gameboardLayout = view.findViewById(R.id.board_grid);
        initGameboard();


        // Overlay
        overlay.requestLayout();
        overlay.invalidate();

        // Grid TouchListener
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
            animateSuccessfulWord(selectedPath);
            wordList.add(word);
            updateWidgets(word.length());
        } else {
            Log.d("GameboardFragment", "Invalid Word: " + word);
        }
        resetHighlights();
        selectedPath.clear();
    }


    private void updateWidgets(int wordLength) {
        // words found
        currentWordsFound++;

        // game score
        if (wordLength <= 2) {
            currentScore += .5;
        } else if (wordLength == 3 || wordLength == 4) {
            currentScore += 1;
        } else if (wordLength == 5) {
            currentScore += 2;
        } else if (wordLength == 6) {
            currentScore += 3;
        } else if (wordLength == 7) {
            currentScore += 5;
        } else {
            currentScore += 11;
        }
        updateCardAmounts();
    }

    private void updateCardAmounts() {
        scoreValue.setText(String.valueOf(currentScore) + "");
        wordCountValue.setText(String.valueOf(currentWordsFound) + "/" + solvedWords.size());
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

                cardView.addView(textView);

                // Add TextView to GridLayout
                gameboardLayout.addView(cardView);
            }
        }
        gameboardLayout.requestLayout();
        gameboardLayout.invalidate();

    }
    private void showEndGameConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("End Game?")
                .setMessage("Viewing the remaining words will end the current game. Are you sure?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // User confirmed, conclude the game
                    concludeGame();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // User canceled, dismiss the dialog
                    dialog.dismiss();
                })
                .create()
                .show();
    }


    private void concludeGame() {
        WordDisplayBottomSheet bottomSheet = new WordDisplayBottomSheet(getRemainingWords(), false);
        bottomSheet.show(getParentFragmentManager(), "bottomSheet");
        resetGame();
    }

    private void resetGame() {

        updateSharedPreferences();

        // reset all attributes to default and spawn a new game
        // Reset score and word list
        currentWordsFound = 0;
        currentScore = 0;
        wordList.clear();
        updateCardAmounts();

        // Clear highlights and reset the board
        resetHighlights();
        gameboardLayout.removeAllViews();
        boggle = new Boggle(requireContext());
        boggle.solveBoard();
        solvedWords = boggle.getWordsFound();
        wordList = new ArrayList<>();
        selectedPath = new ArrayList<>();
        initGameboard();
    }

    private void updateSharedPreferences() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("stats", Context.MODE_PRIVATE);
        // get
        float totalScore = prefs.getFloat("total_score", 0);
        int totalWordsFound = prefs.getInt("total_words_found", 0);
        int totalGamesPlayed = prefs.getInt("total_games_played", 0);
        int totalGamesCompleted = prefs.getInt("total_games_completed", 0);

        // calculate
        totalScore += currentScore;
        totalWordsFound += currentWordsFound;
        totalGamesPlayed++;

        if (currentWordsFound == wordList.size())
            totalGamesCompleted++;

        float averageWordsPerGame = ((float) totalWordsFound)  / ((float) totalGamesPlayed);

        // edit
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("total_score",  totalScore);
        editor.putInt("total_words_found", totalWordsFound);
        editor.putInt("total_games_played", totalGamesPlayed);
        editor.putInt("total_games_completed", totalGamesCompleted);
        editor.putFloat("average_words_per_game", averageWordsPerGame);
        editor.apply();
        editor.commit();
    }

    private GridLayout.LayoutParams getCardLayoutParams(int row, int col) {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(row, 1f); // Equal row weight
        params.columnSpec = GridLayout.spec(col, 1f); // Equal column weight
        params.setMargins(16, 16, 16, 16); // Uniform spacing
        return params;
    }

    private void animateSuccessfulWord(ArrayList<Pair<Integer, Integer>> wordPath) {
        for (Pair<Integer, Integer> position : wordPath) {
            int index = position.first * boggle.getBoardSize() + position.second;
            CardView originalTile = (CardView) gameboardLayout.getChildAt(index);
            animateTile(originalTile);
        }
    }

    private void animateTile(CardView originalTile) {
        overlay = getView().findViewById(R.id.overlay);

        // Dupe card
        CardView duplicateTile = new CardView(requireContext());
        duplicateTile.setLayoutParams(originalTile.getLayoutParams());
        //duplicateTile.setCardBackgroundColor(((ColorDrawable) originalTile.getBackground()).getColor());
        duplicateTile.setCardElevation(originalTile.getCardElevation());
        duplicateTile.setRadius(originalTile.getRadius());

        // Dupe textview
        TextView originalTextView = (TextView) originalTile.getChildAt(0);
        TextView duplicateTextView = new TextView(requireContext());
        duplicateTextView.setText(originalTextView.getText());
        duplicateTextView.setTextSize(originalTextView.getTextSize());
        duplicateTextView.setTextAlignment(originalTextView.getTextAlignment());
        duplicateTextView.setTextColor(originalTextView.getTextColors());
        duplicateTextView.setBackgroundColor(Color.rgb(255, 110, 97));

        duplicateTile.addView(duplicateTextView);

        // Add to invisible layer
        overlay.addView(duplicateTile);
        overlay.requestLayout();
        overlay.invalidate();

        // Get the original tiles position on screen
        int[] originalPosition = new int[2];
        originalTile.getLocationOnScreen(originalPosition);

        // Set the duplicate's initial position to match the original
        duplicateTile.setX(originalPosition[0]);
        duplicateTile.setY(originalPosition[1]);

        // tile shrink animation
        float targetX = originalPosition[0] + (float) (Math.random() * 200 - 100); // Random horizontal movement
        float targetY = originalPosition[1] + 800; // Fly towards the bottom of the screen
        ObjectAnimator translateX = ObjectAnimator.ofFloat(duplicateTile, "translationX", duplicateTile.getX(), targetX);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(duplicateTile, "translationY", duplicateTile.getY(), targetY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(duplicateTile, "scaleX", .5f, 0.1f); // Shrink the tile
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(duplicateTile, "scaleY", .5f, 0.1f);

        // Combine animations
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateX, translateY, scaleX, scaleY);
        animatorSet.setDuration(1500);

        // need to remove the duplicate
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                overlay.removeView(duplicateTile); // Remove duplicate tile
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        // Start the animation
        animatorSet.start();
    }

    private ArrayList<String> getRemainingWords() {
        ArrayList<String> newList = new ArrayList<>();
        for (String word : solvedWords) {
            if (!wordList.contains(word)) {
                newList.add(word);
            }
        }
        return newList;
    }
}