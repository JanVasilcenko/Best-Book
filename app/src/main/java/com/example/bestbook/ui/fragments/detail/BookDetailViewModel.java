package com.example.bestbook.ui.fragments.detail;

import android.app.Application;
import android.widget.RatingBar;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bestbook.architecture.adapters.CommentAdapter;
import com.example.bestbook.architecture.repositories.AuthenticationRepository;
import com.example.bestbook.architecture.repositories.BookFirebaseRepository;
import com.example.bestbook.architecture.repositories.BookRepository;
import com.example.bestbook.architecture.database.ReferenceRepository;
import com.example.bestbook.model.Book;
import com.example.bestbook.model.Comment;
import com.example.bestbook.model.FavouriteReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class BookDetailViewModel extends AndroidViewModel {
    BookRepository bookRepository;
    ReferenceRepository referenceRepository;
    AuthenticationRepository authenticationRepository;
    BookFirebaseRepository bookFirebaseRepository;

    public BookDetailViewModel(Application app) {
        super(app);
        this.bookRepository = BookRepository.getInstance();
        this.referenceRepository = ReferenceRepository.getInstance(app);
        this.authenticationRepository = AuthenticationRepository.getInstance(app);
        this.bookFirebaseRepository = BookFirebaseRepository.getInstance();
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
    public String getUserEmail(){return authenticationRepository.getUserEmail();}
    public String getUserID(){return authenticationRepository.getUserID();}
    public void getBookRating(String bookID, RatingBar ratingBar){ bookFirebaseRepository.getBookRating(bookID,getUserID(),ratingBar);}
    public void setRating(String bookID, float rating){bookFirebaseRepository.setRating(bookID,getUserID(),rating);}
    public void addReview(String bookID, String comment, float rating){ bookFirebaseRepository.addReview(bookID,getUserID(),comment,rating);}
    public void fetchComments(String bookID, ArrayList<Comment> comments, CommentAdapter commentAdapter){bookFirebaseRepository.fetchComments(bookID,comments,commentAdapter);}
}
