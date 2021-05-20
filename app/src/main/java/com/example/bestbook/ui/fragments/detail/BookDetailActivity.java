package com.example.bestbook.ui.fragments.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.bestbook.R;
import com.example.bestbook.adapters.CommentAdapter;
import com.example.bestbook.model.Book;
import com.example.bestbook.model.Comment;
import com.example.bestbook.model.FavouriteReference;
import com.example.bestbook.ui.fragments.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView bookCover;
    private TextView title;
    private TextView author;
    private TextView publisher;
    private TextView pageCount;
    private TextView description;
    private BookDetailViewModel bookDetailViewModel;
    private ToggleButton favourite;
    private RatingBar ratingBar;
    private EditText addComment;
    private RecyclerView commentsRecycler;
    private CommentAdapter commentAdapter;
    private Book book;
    private ArrayList<Comment> comments = new ArrayList<>();

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
        ratingBar = findViewById(R.id.rating);
        addComment = findViewById(R.id.comments);
        addComment.setOnEditorActionListener(editorListener);
        commentsRecycler = findViewById(R.id.commentsRecyclerView);
        favourite = findViewById(R.id.favourite);

        bookDetailViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(BookDetailViewModel.class);

        //Setting recycler view
        commentsRecycler.hasFixedSize();
        commentsRecycler.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this,comments);
        commentsRecycler.setAdapter(commentAdapter);


        book = (Book) getIntent().getSerializableExtra(HomeFragment.BOOK_KEY);
        bookDetailViewModel.loadBookInfo(book);

        //Load book data
        bookDetailViewModel.getBook().observe(this, book1 -> {
            Picasso.with(this).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.no_cover_available).into(bookCover);
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
            publisher.setText(book.getPublisher());
            pageCount.setText(book.getNumOfPages());
            description.setText(book.getDescription());
            this.setTitle(book.getTitle());
        } );

        FavouriteCheckboxListener();

        GetMyRating();

        FetchComments();

        FavouriteCheck();

        RatingBarListener();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void FavouriteCheckboxListener()
    {
        favourite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    bookDetailViewModel.insert(new FavouriteReference(book.getId(),bookDetailViewModel.getUserEmail()));
                }
                else
                    bookDetailViewModel.deleteReference(book.getId());
            }
        });
    }

    private void RatingBarListener()
    {
        //Apply rating if the rating bar value is changed
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                bookDetailViewModel.setRating(book.getId(),rating);
            }
        });
    }

    //Adding a review
    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(EditorInfo.IME_ACTION_DONE == actionId)
            {
                bookDetailViewModel.addReview(book.getId(),addComment.getText().toString(),ratingBar.getRating());

                addComment.setText("");
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
                FetchComments();
            }
            return false;
        }
    };

    private void FavouriteCheck()
    {
        //If the book is in favourites check favourite checkbox
        bookDetailViewModel.getAllReferences().observe(this, references ->{

            for (FavouriteReference r : references) {
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

    private void FetchComments()
    {
        bookDetailViewModel.fetchComments(book.getId(),comments,commentAdapter);
    }

    private void GetMyRating()
    {
        bookDetailViewModel.getBookRating(book.getId(),ratingBar);
    }
}