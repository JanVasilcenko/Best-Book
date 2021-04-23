package com.example.bestbook.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.bestbook.R;
import com.example.bestbook.model.Book;
import com.example.bestbook.model.FavouriteReference;
import com.example.bestbook.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView bookCover;
    private TextView title;
    private TextView author;
    private TextView publisher;
    private TextView pageCount;
    private TextView description;
    private BookDetailViewModel bookDetailViewModel;
    private ToggleButton favourite;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        bookCover = findViewById(R.id.bookCover);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        publisher = findViewById(R.id.publisher);
        pageCount = findViewById(R.id.pageCount);
        description = findViewById(R.id.description);
        bookDetailViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(BookDetailViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();

        Book book = (Book) getIntent().getSerializableExtra(HomeFragment.BOOK_KEY);
        bookDetailViewModel.loadBookInfo(book);

        bookDetailViewModel.getBook().observe(this, book1 -> {
            Picasso.with(this).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.no_cover_available).into(bookCover);
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
            publisher.setText(book.getPublisher());
            pageCount.setText(book.getNumOfPages());
            description.setText(book.getDescription());
            this.setTitle(book.getTitle());
        } );

        favourite = findViewById(R.id.favourite);


        favourite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    bookDetailViewModel.insert(new FavouriteReference(book.getId(),firebaseAuth.getCurrentUser().getEmail()));
                }
                else
                bookDetailViewModel.deleteReference(book.getId());
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //bookDetailViewModel.insert(new FavouriteReference("test","test"));
        bookDetailViewModel.getAllReferences().observe(this, references ->{

            for (FavouriteReference r:references) {
                if (r.getBookID().equals(book.getId()))
                {
                    favourite.setChecked(true);
                    break;
                }else
                    {
                        favourite.setChecked(false);
                    }
            }


        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}