package com.example.bestbook.ui.favourite;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bestbook.architecture.BookRepository;
import com.example.bestbook.database.ReferenceRepository;
import com.example.bestbook.model.Book;
import com.example.bestbook.model.FavouriteReference;

import java.util.ArrayList;
import java.util.List;

public class FavouriteViewModel extends AndroidViewModel {
    private ReferenceRepository referenceRepository;
    private BookRepository bookRepository;

    public FavouriteViewModel(Application app)
    {
        super(app);
        referenceRepository = ReferenceRepository.getInstance(app);
        bookRepository = BookRepository.getInstance();
    }

    public LiveData<List<FavouriteReference>> getAllReferences(){return referenceRepository.getAllReferences();}
    public void getMyBooks(ArrayList<FavouriteReference> references){bookRepository.getMyBooks(references);}
    public LiveData<ArrayList<Book>> getBooks(){return bookRepository.getMyBooks();}
}
