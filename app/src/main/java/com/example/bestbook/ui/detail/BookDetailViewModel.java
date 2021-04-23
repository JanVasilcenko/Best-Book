package com.example.bestbook.ui.detail;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bestbook.architecture.BookRepository;
import com.example.bestbook.database.ReferenceRepository;
import com.example.bestbook.model.Book;
import com.example.bestbook.model.FavouriteReference;

import java.util.List;


public class BookDetailViewModel extends AndroidViewModel {
    BookRepository bookRepository;
    ReferenceRepository referenceRepository;

    public BookDetailViewModel(Application app) {
        super(app);
        this.bookRepository = BookRepository.getInstance();
        this.referenceRepository = ReferenceRepository.getInstance(app);
    }

    public void loadBookInfo(Book book)
    {
        bookRepository.getBookInfo(book);
    }

    LiveData<Book> getBook()
    {
        return bookRepository.getBook();
    }

    public void insert(FavouriteReference reference){referenceRepository.insert(reference);}
    public LiveData<List<FavouriteReference>> getAllReferences(){return referenceRepository.getAllReferences();}
    public void deleteReference(String bookID){referenceRepository.deleteAll(bookID);}
}
