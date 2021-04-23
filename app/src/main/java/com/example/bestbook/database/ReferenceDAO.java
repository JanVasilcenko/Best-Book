package com.example.bestbook.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bestbook.model.FavouriteReference;

import java.util.List;

@Dao
public interface ReferenceDAO {
    @Insert
    void insert(FavouriteReference reference);

    @Query("SELECT * FROM favourite_table")
    List<FavouriteReference> getReferences();

    @Query("DELETE FROM favourite_table WHERE bookID=:bookID")
    void deleteReference(String bookID);

    @Query("SELECT * FROM favourite_table")
    LiveData<List<FavouriteReference>> getAllReferences();
}
