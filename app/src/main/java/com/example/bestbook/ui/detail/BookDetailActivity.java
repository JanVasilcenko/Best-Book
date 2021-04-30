package com.example.bestbook.ui.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.bestbook.R;
import com.example.bestbook.model.Book;
import com.example.bestbook.model.FavouriteReference;
import com.example.bestbook.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private RatingBar ratingBar;
    FirebaseDatabase database;
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
        ratingBar = findViewById(R.id.rating);
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

        database = FirebaseDatabase.getInstance("https://best-book-3c38c-default-rtdb.europe-west1.firebasedatabase.app/");

        DatabaseReference reference = database.getReference().child("Books").child(book.getId()).child("Rating");
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot data : task.getResult().getChildren())
                {
                    if(data.getKey().equals(firebaseAuth.getCurrentUser().getUid()))
                    {
                        ratingBar.setRating(Float.parseFloat(data.getValue().toString()));
                        break;
                    }
                }
            }
        });

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

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                DatabaseReference newReference = database.getReference().child("Books").child(book.getId()).child("Rating").child(firebaseAuth.getCurrentUser().getUid());
                newReference.setValue(rating);
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