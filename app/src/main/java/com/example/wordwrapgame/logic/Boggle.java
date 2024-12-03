package com.example.wordwrapgame.logic;

import android.content.Context;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordwrapgame.R;

import java.io.InputStream;
import java.util.*;

public class Boggle {
    private static final int BOARD_SIZE = 4;

    private Dictionary dict;
    private Dictionary wordsFound;

    private DiceSet diceSet;

    // Boards
    private Die[][] diceBoard;
    private String[][] board;
    private int[][] visited;

    public Boggle(Context context) {

        //AppCompatActivity appCompatActivity = new AppCompatActivity();

        InputStream is = context.getResources().openRawResource(R.raw.twl066);
        this.dict = new Dictionary(is);
        this.wordsFound = new Dictionary();

        is = context.getResources().openRawResource(R.raw.dieconfig);
        diceSet = new DiceSet(is);
        board = new String[BOARD_SIZE][BOARD_SIZE];
        diceBoard = new Die[BOARD_SIZE][BOARD_SIZE];
        configureBoardWithDie();

        visited = new int[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                visited[i][j] = 0;
            }
        }
    }

    public Boggle(String filename) {
        this.dict = new Dictionary(filename);
        this.wordsFound = new Dictionary();

        board = new String[BOARD_SIZE][BOARD_SIZE];
        visited = new int[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = "";
            }
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                visited[i][j] = 0;
            }
        }

    }

    public Boggle(String filename, String[][] board) {
        this.dict = new Dictionary(filename);
        this.wordsFound = new Dictionary();

        this.board = new String[BOARD_SIZE][BOARD_SIZE];
        setBoard(board);

        visited = new int[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                visited[i][j] = 0;
            }
        }

    }

    public void setBoard(String[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    public void solveBoard() {
        // we need to solve the board from every position
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++) {
                solveBoardHelper(i, j, "", 1);
                resetVisited();
            }
        }
    }

    public Die[][] getDiceBoard() {
        return diceBoard;
    }

    public ArrayList<String> getWordsFound() {
        return wordsFound.getWords();
    }

    public void printSolvedWords() {
        wordsFound.printWords();
    }
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public String returnValueAtBoardPos(int r, int c) {
        return board[r][c];
    }

    // private methods
    private void configureBoardWithDie(){
        // With the diceSet use the showingside to configure a board


        ArrayList<Die> dieLeft = new ArrayList<>(Arrays.asList(diceSet.getDice()));
        int size = dieLeft.size();
        Random rand = new Random();
        int colCnt = 0; // where we are positioned in the columns
        int rowCnt = 0; // where we are positioned in the rows

        while (size > 0) {
            // pick a die at random
            if (colCnt != BOARD_SIZE) {
                int index = rand.nextInt(size);
                board[rowCnt][colCnt] = dieLeft.get(index).getShowingSide();
                diceBoard[rowCnt][colCnt] = dieLeft.get(index);
                dieLeft.remove(index);

                colCnt++;
                if (colCnt == BOARD_SIZE) {
                    rowCnt++;
                    colCnt = 0;
                }
                size--;
            }
        }

        printBoard();

    }

    private void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        printDieIDBoard();
    }

    private void printDieIDBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(diceBoard[i][j].getIdNum() + " ");
            }
            System.out.println();
        }
    }

    private void solveBoardHelper(int row, int col, String prefix, int numLetters) {
        if(row < 0 || col < 0 || row >= BOARD_SIZE || col >= BOARD_SIZE) // BOUNDS CHECK
            return;
        if (!dict.isPrefix(prefix+board[row][col])) // if the prefix is not a valid prefix, no more searches in this branch
            return;
        if (visited[row][col] > 0) // current letter has already used
            return;
        visited[row][col] = numLetters;

        if (dict.isWord(prefix+board[row][col])) {
            if(!wordsFound.isWord(prefix+board[row][col])) { // Word doesn't already exist
                wordsFound.addWord(prefix+board[row][col]);

                // print board logic here but i dont think we need this
            }
        }

        solveBoardHelper(row-1, col, prefix + board[row][col], numLetters); // N
        solveBoardHelper(row-1, col+1, prefix + board[row][col], numLetters); // NE
        solveBoardHelper(row, col+1, prefix + board[row][col], numLetters); // E
        solveBoardHelper(row+1, col+1, prefix + board[row][col], numLetters); // SE

        solveBoardHelper(row+1, col, prefix + board[row][col], numLetters); // S
        solveBoardHelper(row+1, col-1, prefix + board[row][col], numLetters); // SW
        solveBoardHelper(row, col-1, prefix + board[row][col], numLetters); // W
        solveBoardHelper(row-1, col-1, prefix + board[row][col], numLetters); // NW

        visited[row][col] = 0; // remove breadcrumb
    }

    private void resetVisited(){
        visited = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                visited[i][j] = 0;
            }
        }
    }

}
