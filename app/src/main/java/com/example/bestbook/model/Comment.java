package com.example.bestbook.model;

public class Comment {
    private String text;
    private float rating;

    public Comment(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public float getRating() {
        return rating;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
