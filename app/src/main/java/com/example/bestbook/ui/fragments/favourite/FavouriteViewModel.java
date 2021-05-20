package com.example.bestbook.ui.fragments.favourite;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bestbook.adapters.BookAdapter;
import com.example.bestbook.repositories.AuthenticationRepository;
import com.example.bestbook.repositories.BookFirebaseRepository;
import com.example.bestbook.webservices.BookRepository;
import com.example.bestbook.database.ReferenceRepository;
import com.example.bestbook.model.Book;
import com.example.bestbook.model.FavouriteReference;

import java.util.ArrayList;
import java.util.List;

public class FavouriteViewModel extends AndroidViewModel {
    private ReferenceRepository referenceRepository;
    private BookRepository bookRepository;
    private BookFirebaseRepository bookFirebaseRepository;
    private AuthenticationRepository authenticationRepository;

    public FavouriteViewModel(Application app)
    {
        super(app);
        referenceRepository = ReferenceRepository.getInstance(app);
        bookRepository = BookRepository.getInstance();
        bookFirebaseRepository = BookFirebaseRepository.getInstance();
        authenticationRepository = AuthenticationRepository.getInstance(app);
    }

    public LiveData<List<FavouriteReference>> getAllReferences(){return referenceRepository.getAllReferences();}
    public void getMyBooks(ArrayList<FavouriteReference> references){bookRepository.getMyBooks(references);}
    public LiveData<ArrayList<Book>> getBooks(){return bookRepository.getMyBooks();}
    public ArrayList<Book> getBookAverageRating(ArrayList<Book> books, BookAdapter bookAdapter)
    {
        return bookFirebaseRepository.getAverageBookRating(books, bookAdapter);
    }
    public String getUserEmail(){return authenticationRepository.getUserEmail();}
}
