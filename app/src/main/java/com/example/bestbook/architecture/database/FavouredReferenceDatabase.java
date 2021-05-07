package com.example.bestbook.architecture.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bestbook.model.FavouriteReference;

@Database(entities = {FavouriteReference.class}, version = 1, exportSchema = false)
public abstract class FavouredReferenceDatabase extends RoomDatabase {
    private static FavouredReferenceDatabase instance;
    public abstract ReferenceDAO referenceDAO();

    public static synchronized FavouredReferenceDatabase getInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FavouredReferenceDatabase.class, "reference_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
