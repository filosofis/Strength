package com.lundqvist.oscar.strength.model;

/**
 * Created by Oscar on 2018-03-24.
 */

public class Program {
    public String title;
    public String author;
    public int duration;
    public String description;

    public Program() {
        //required for datasnapshot
    }

    public Program(String title, String author, int duration, String description) {
        this.title = title;
        this.author = author;
        this.duration = duration;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Program{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
