package com.lundqvist.oscar.strength;

public class Utility {

    public int calculateVolume(){

        return 0;
    }

    /**
     *  Brzycki Formula
     */
    public int oneRepMaxCalc(int reps, int weight){
        System.out.println("Reps: " +  reps + " Weight: " + weight);
        float result = (float)weight * 36 / (37 - (float)reps);
        System.out.println(Math.round(result));
        return Math.round(result);
    }
}
