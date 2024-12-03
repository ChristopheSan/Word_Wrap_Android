package com.example.wordwrapgame.logic;

import java.util.Random;

public class Die {
    private static final int SIDE_NUM = 6;
    private static int count = 0;

    private int idNum;
    private String[] sides;

    private String showingSide;

    public Die(String[] sides) {
        this.idNum = count++;
        this.showingSide = sides[0];
        this.sides = new String[SIDE_NUM];
        setSides(sides);
    }

    public void setSides(String[] sides) {
        for (int i = 0; i < SIDE_NUM; i++) {
            this.sides[i] = sides[i];
        }
    }

    public String[] getSides() {
        return sides;
    }

    public void setShowingSide(String showingSide) {
        this.showingSide = showingSide;
    }
    public String getShowingSide() {
        return showingSide;
    }

    public void rollDie(){
        Random rand = new Random();
        int index = rand.nextInt(SIDE_NUM);
        showingSide = sides[index];
    }

    public int getIdNum() {
        return idNum;
    }

    @Override
    public String toString() {
        return "Die-" + idNum + " Showing Side: " + showingSide;
    }
}
