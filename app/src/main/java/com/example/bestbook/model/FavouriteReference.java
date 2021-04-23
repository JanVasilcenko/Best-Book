package com.example.bestbook.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_table")
public class FavouriteReference {
@PrimaryKey(autoGenerate = true)
private int id;
private String bookID;
private String email;

    public FavouriteReference(String bookID, String email) {
        this.bookID = bookID;
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getBookID() {
        return bookID;
    }

    public String getEmail() {
        return email;
    }
}
