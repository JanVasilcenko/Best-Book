package com.example.bestbook.architecture.repositories;

import android.os.Build;
import android.util.Log;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.bestbook.architecture.adapters.BookAdapter;
import com.example.bestbook.architecture.adapters.CommentAdapter;
import com.example.bestbook.model.Book;
import com.example.bestbook.model.BookComparator;
import com.example.bestbook.model.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class BookFirebaseRepository {
    private FirebaseDatabase firebaseDatabase;
    private static BookFirebaseRepository instance;

    public BookFirebaseRepository()
    {
        firebaseDatabase = FirebaseDatabase.getInstance("https://best-book-3c38c-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    public static BookFirebaseRepository getInstance()
    {
        if(instance == null)
        {
            instance = new BookFirebaseRepository();
        }
        return instance;
    }

    public ArrayList<Book> getAverageBookRating(ArrayList<Book> books, BookAdapter bookAdapter)
    {
        for (Book book:books)
        {
            DatabaseReference reference = firebaseDatabase.getReference().child("Books");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot s: snapshot.getChildren()) {
                        if (s.getKey().equals(book.getId())) {
                            DatabaseReference newReference = firebaseDatabase.getReference().child("Books").child(book.getId()).child("Rating");
                            newReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    float counter = 0f;
                                    int count = 0;
                                    for (DataSnapshot s : snapshot.getChildren()) {
                                        counter += Float.parseFloat(s.getValue().toString());
                                        count++;
                                    }
                                    book.setRating(counter / count);
                                    bookAdapter.notifyDataSetChanged();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return books;
    }

    public void fetchComments(String bookID, ArrayList<Comment> comments, CommentAdapter commentAdapter)
    {
        DatabaseReference commentsReference = firebaseDatabase.getReference().child("Books").child(bookID).child("Comments");
        commentsReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                comments.clear();
                for(DataSnapshot data: task.getResult().getChildren())
                {
                    Comment newComment = new Comment(data.child("value").getValue().toString());
                    newComment.setRating(Float.parseFloat(data.child("rating").getValue().toString()));
                    comments.add(newComment);
                }
                commentAdapter.notifyDataSetChanged();
            }
        });
    }

    public void addReview(String bookID, String userID,String comment, float rating)
    {
        DatabaseReference valueCommentsReference = firebaseDatabase.getReference().child("Books").child(bookID).child("Comments").child(userID).child("value");
        valueCommentsReference.setValue(comment);
        DatabaseReference ratingCommentsReference = firebaseDatabase.getReference().child("Books").child(bookID).child("Comments").child(userID).child("rating");
        ratingCommentsReference.setValue(rating);
    }

    public void setRating(String bookID, String userID, float rating)
    {
        DatabaseReference newReference = firebaseDatabase.getReference().child("Books").child(bookID).child("Rating").child(userID);
        newReference.setValue(rating);
    }

    public void getBookRating(String bookID, String userID, RatingBar ratingBar)
    {
        DatabaseReference reference = firebaseDatabase.getReference().child("Books").child(bookID).child("Rating");
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot data : task.getResult().getChildren())
                {
                    if(data.getKey().equals(userID))
                    {
                        ratingBar.setRating(Float.parseFloat(data.getValue().toString()));
                        break;
                    }
                }
            }
        });
    }
}
