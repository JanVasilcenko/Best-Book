package com.example.bestbook.ui.fragments.favourite;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.bestbook.architecture.adapters.BookAdapter;
import com.example.bestbook.model.Book;
import com.example.bestbook.model.FavouriteReference;
import com.example.bestbook.ui.fragments.detail.BookDetailActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment implements BookAdapter.OnListItemClickListener{

    public static final String BOOK_KEY = "book";
    private RecyclerView favouriteBooksList;
    private FavouriteViewModel favouriteViewModel;
    private BookAdapter bookAdapter;
    private ArrayList<Book> books;
    private ArrayList<FavouriteReference> myReferences;
    private ProgressBar progressBar;
    private TextView noBooksYet;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favourite, container, false);


        favouriteBooksList = root.findViewById(R.id.favouriteBookRecycleView);
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
        progressBar = root.findViewById(R.id.loadingBooksProgressBar);
        books = new ArrayList<>();
        myReferences = new ArrayList<>();
        noBooksYet = root.findViewById(R.id.noBooksYet);
        bookAdapter = new BookAdapter(getContext(),books,this);

        setupRecyclerView();

        progressBar.setVisibility(ProgressBar.VISIBLE);

        noBooksYet.setVisibility(TextView.GONE);

        getActivity().setTitle(R.string.favourite);

        return root;
    }

    private void setupRecyclerView() {
        favouriteBooksList.hasFixedSize();
        favouriteBooksList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        favouriteBooksList.setAdapter(bookAdapter);
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

                    if(favouriteViewModel.getUserEmail().equals(r.getEmail()))
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

            ArrayList<Book> booksWithRating = favouriteViewModel.getBookAverageRating(books1,bookAdapter);

            books.addAll(booksWithRating);
            bookAdapter.notifyDataSetChanged();
        });
    }
}
