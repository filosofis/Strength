package com.lundqvist.oscar.strength.model;

/**
 * Created by Oscar on 2018-03-25.
 */

public class Exercise {
    private int workout;
    private String name;
    private int weight;
    private int sets;
    private int reps;
    private int time;
    private String note;

    public Exercise(int workout, String name, int weight, int sets, int reps, int time, String note) {
        this.name = name;
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
        this.workout = workout;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "workout=" + workout +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", sets=" + sets +
                ", reps=" + reps +
                ", time=" + time +
                ", note='" + note + '\'' +
                '}';
    }

    public int getWorkout() {
        return workout;
    }

    public void setWorkout(int workout) {
        this.workout = workout;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
