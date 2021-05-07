package com.example.bestbook.ui.fragments.home;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.bestbook.R;

import com.example.bestbook.architecture.adapters.BookAdapter;
import com.example.bestbook.model.Book;
import com.example.bestbook.ui.fragments.detail.BookDetailActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements BookAdapter.OnListItemClickListener{

    public static final String BOOK_KEY = "book";
    private HomeViewModel homeViewModel;
    private RecyclerView searchBooksList;
    private BookAdapter booksAdapter;
    private ProgressBar progressBar;
    private TextView noSearchedBooks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        searchBooksList = root.findViewById(R.id.bookSearchRecycleView);
        progressBar = root.findViewById(R.id.loadingBooksProgressBar);
        noSearchedBooks = root.findViewById(R.id.noBooksSearched);

        searchBooksList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        searchBooksList.hasFixedSize();

        setHasOptionsMenu(true);


        ArrayList<Book> books = new ArrayList<>();
        booksAdapter = new BookAdapter(getContext(),books,this);
        searchBooksList.setAdapter(booksAdapter);

        homeViewModel.getSearchedBooks().observe(getViewLifecycleOwner(),bookCollection -> {
            noSearchedBooks.setVisibility(View.GONE);
            progressBar.setVisibility(ProgressBar.GONE);
            books.clear();

            ArrayList<Book> booksWithRating = homeViewModel.getBookAverageRating(bookCollection, booksAdapter);

            books.addAll(booksWithRating);
            booksAdapter.notifyDataSetChanged();
        });

        noSearchedBooks.setVisibility(View.VISIBLE);

        return root;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu,menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                homeViewModel.searchBooks(query);

                searchView.clearFocus();
                searchView.setQuery("",false);
                searchView.setIconified(true);
                searchItem.collapseActionView();

                getActivity().setTitle(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent toBookDetailIntent = new Intent(getActivity(), BookDetailActivity.class);
        toBookDetailIntent.putExtra(BOOK_KEY, booksAdapter.books.get(clickedItemIndex));
        startActivity(toBookDetailIntent);
    }
}