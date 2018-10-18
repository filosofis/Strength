package com.lundqvist.oscar.strength.model;

/**
 * Created by Oscar on 2018-03-25.
 */

public class Exercise {
    private String name;
    private int weight;
    private int sets;
    private int reps;

    @Override
    public String toString() {
        return "Exercise{" +
                "name='" + name + '\'' +
                ", sets=" + sets +
                ", reps=" + reps +
                ", weight=" + weight +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Exercise(String name) {

        this.name = name;
    }
}
