package com.lundqvist.oscar.strength;

public class Utility {

    public int calculateVolume(){

        return 0;
    }

    /**
     *  Brzycki Formula
     */
    public int oneRepMaxCalc(int reps, int weight){
        return weight*(36/(37-reps));
    }
}
