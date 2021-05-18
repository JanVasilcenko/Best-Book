package com.example.bestbook.ui.fragments.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.bestbook.architecture.adapters.BookAdapter;
import com.example.bestbook.architecture.repositories.BookFirebaseRepository;
import com.example.bestbook.architecture.repositories.BookRepository;
import com.example.bestbook.model.Book;

import java.util.ArrayList;


public class HomeViewModel extends ViewModel {
    private BookRepository bookRepository;
    private BookFirebaseRepository bookFirebaseRepository;

    public HomeViewModel()
    {
        bookRepository = BookRepository.getInstance();
        bookFirebaseRepository = BookFirebaseRepository.getInstance();
    }

    public void searchBooks(String name)
    {
        bookRepository.searchForBooks(name);
    }

    LiveData<ArrayList<Book>> getSearchedBooks()
    {
        return bookRepository.getSearchedBooks();
    }

    public ArrayList<Book> getBookAverageRating(ArrayList<Book> books, BookAdapter bookAdapter)
    {

        return bookFirebaseRepository.getAverageBookRating(books, bookAdapter);
    }



}
