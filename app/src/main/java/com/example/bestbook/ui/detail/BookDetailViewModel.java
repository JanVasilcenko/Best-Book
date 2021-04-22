package com.example.bestbook.ui.detail;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bestbook.architecture.BookRepository;
import com.example.bestbook.model.Book;


public class BookDetailViewModel extends AndroidViewModel {
    BookRepository bookRepository;

    public BookDetailViewModel(Application app) {
        super(app);
        this.bookRepository = BookRepository.getInstance();
    }

    public void loadBookInfo(Book book)
    {
        bookRepository.getBookInfo(book);
    }

    LiveData<Book> getBook()
    {
        return bookRepository.getBook();
    }

}
