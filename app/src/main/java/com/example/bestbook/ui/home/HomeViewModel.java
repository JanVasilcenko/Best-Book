package com.example.bestbook.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.bestbook.architecture.BookRepository;
import com.example.bestbook.model.Book;

import java.util.ArrayList;


public class HomeViewModel extends ViewModel {
BookRepository bookRepository;

public HomeViewModel(){bookRepository = BookRepository.getInstance();}

//public void fetchBook(String id){bookRepository.fetchBook(id);}
LiveData<Book> getBook(){return bookRepository.getBook();}
public void searchBooks(String name)
{
    bookRepository.searchForBooks(name);
}
LiveData<ArrayList<Book>> getSearchedBooks()
{
    return bookRepository.getSearchedBooks();
}


}
