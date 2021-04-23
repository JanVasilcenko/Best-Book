package com.example.bestbook.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bestbook.model.FavouriteReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReferenceRepository {
    private ReferenceDAO referenceDAO;
    private static  ReferenceRepository instance;
    private LiveData<List<FavouriteReference>> allReferences;
    private ExecutorService executorService;

    public static ReferenceRepository getInstance(Application application)
    {
        if(instance==null)
        {
            instance = new ReferenceRepository(application);
        }
        return instance;
    }

    private ReferenceRepository(Application application)
    {
        FavouredReferenceDatabase database = FavouredReferenceDatabase.getInstance(application);
        referenceDAO = database.referenceDAO();
        allReferences = referenceDAO.getAllReferences();
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<FavouriteReference>> getAllReferences(){return allReferences;}

    public void insert(FavouriteReference reference){executorService.execute(()-> referenceDAO.insert(reference));}
    public void deleteAll(String bookID){executorService.execute(()-> referenceDAO.deleteReference(bookID));}
}
