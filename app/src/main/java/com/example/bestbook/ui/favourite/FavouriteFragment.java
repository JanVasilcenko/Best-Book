package com.example.bestbook.ui.favourite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestbook.R;
import com.example.bestbook.architecture.BookAdapter;
import com.example.bestbook.model.Book;
import com.example.bestbook.model.FavouriteReference;
import com.example.bestbook.ui.detail.BookDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment implements BookAdapter.OnListItemClickListener{

    public static final String BOOK_KEY = "book";
    private RecyclerView favouriteBooksList;
    private FavouriteViewModel favouriteViewModel;
    private BookAdapter bookAdapter;
    private ArrayList<Book> books;
    private ArrayList<FavouriteReference> myReferences;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private TextView noBooksYet;
    FirebaseDatabase database;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favourite, container, false);

        //Instantiating components and dependencies
        favouriteBooksList = root.findViewById(R.id.favouriteBookRecycleView);
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
        progressBar = root.findViewById(R.id.loadingBooksProgressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        books = new ArrayList<>();
        myReferences = new ArrayList<>();
        noBooksYet = root.findViewById(R.id.noBooksYet);
        bookAdapter = new BookAdapter(getContext(),books,this);

        //Setting up database
        database = FirebaseDatabase.getInstance("https://best-book-3c38c-default-rtdb.europe-west1.firebasedatabase.app/");

        //Settings for recycler view
        favouriteBooksList.hasFixedSize();
        favouriteBooksList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        favouriteBooksList.setAdapter(bookAdapter);

        //Setting progress bar on
        progressBar.setVisibility(ProgressBar.VISIBLE);

        //Setting no books yet off
        noBooksYet.setVisibility(TextView.GONE);

        return root;
    }

    //If item click pass data and navigate to book details
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent toBookDetailIntent = new Intent(getActivity(), BookDetailActivity.class);
        toBookDetailIntent.putExtra(BOOK_KEY, bookAdapter.books.get(clickedItemIndex));
        startActivity(toBookDetailIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        //For each favourite reference get book
        favouriteViewModel.getAllReferences().observe(getViewLifecycleOwner(),favouriteReferences -> {
            myReferences.clear();
            if (favouriteReferences.isEmpty())
            {
                progressBar.setVisibility(ProgressBar.GONE);
                noBooksYet.setVisibility(TextView.VISIBLE);
                favouriteViewModel.getMyBooks(null);
            }
            else
            {
                for (FavouriteReference r : favouriteReferences)
                {

                    if(firebaseAuth.getCurrentUser().getEmail().equals(r.getEmail()))
                    {
                        myReferences.add(r);
                    }
                }
                favouriteViewModel.getMyBooks(myReferences);
                bookAdapter.notifyDataSetChanged();
            }
        });

        //Display books into recycler view
        favouriteViewModel.getBooks().observe(getViewLifecycleOwner(),books1 -> {
            progressBar.setVisibility(ProgressBar.GONE);
            books.clear();

            //Get book ratings
            for (Book b:books1)
            {
                DatabaseReference reference = database.getReference().child("Books");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot s : snapshot.getChildren())
                        {
                            if (s.getKey().equals(b.getId()))
                            {
                                DatabaseReference newReference = database.getReference().child("Books").child(b.getId()).child("Rating");
                                newReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        float counter = 0f;
                                        int count = 0;
                                        for (DataSnapshot s : snapshot.getChildren()) {
                                            counter += Float.parseFloat(s.getValue().toString());
                                            count++;
                                        }
                                        b.setRating(counter / count);
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
            books.addAll(books1);
            bookAdapter.notifyDataSetChanged();
        });
    }
}
