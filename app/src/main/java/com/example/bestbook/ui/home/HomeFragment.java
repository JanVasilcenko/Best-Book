package com.example.bestbook.ui.home;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;


import com.example.bestbook.R;

import com.example.bestbook.architecture.BookAdapter;
import com.example.bestbook.model.Book;
import com.example.bestbook.ui.detail.BookDetailActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements BookAdapter.OnListItemClickListener{

    public static  final String BOOK_KEY = "book";
    private HomeViewModel homeViewModel;
    RecyclerView booksList;
    BookAdapter booksAdapter;
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        booksList = root.findViewById(R.id.recycleView);
        progressBar = root.findViewById(R.id.progressBar3);
        booksList.hasFixedSize();
        setHasOptionsMenu(true);
        booksList.setLayoutManager(new LinearLayoutManager(this.getContext()));


        ArrayList<Book> books = new ArrayList<>();
        booksAdapter = new BookAdapter(getContext(),books,this);
        booksList.setAdapter(booksAdapter);


        homeViewModel.getSearchedBooks().observe(getViewLifecycleOwner(),bookCollection -> {
            progressBar.setVisibility(ProgressBar.GONE);
            books.clear();
            books.addAll(bookCollection);
            booksAdapter.notifyDataSetChanged();
        });

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