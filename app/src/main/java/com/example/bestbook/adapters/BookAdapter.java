package com.example.bestbook.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bestbook.R;
import com.example.bestbook.model.Book;
import com.example.bestbook.model.BookComparator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    final private OnListItemClickListener onListItemClickListener;
    public ArrayList<Book> books;
    private Context context;

    public BookAdapter(Context context,ArrayList<Book> books,OnListItemClickListener onListItemClickListener) {

        this.books = books;
        this.context = context;
        this.onListItemClickListener = onListItemClickListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_item, parent,false);
        return new ViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        SortByRating();
        viewHolder.bookName.setText(books.get(position).getTitle());
        viewHolder.authorName.setText(books.get(position).getAuthor());
        viewHolder.ratingBar.setRating(books.get(position).getRating());
        Picasso.with(this.context).load(Uri.parse(books.get(position).getMediumCoverUrl())).error(R.drawable.no_cover_available).into(viewHolder.cover);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void SortByRating()
    {
        if (!books.isEmpty()) {
            books.sort(new BookComparator());
        }
    }

    public int getItemCount(){
        return books.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView bookName;
        ImageView cover;
        TextView authorName;
        RatingBar ratingBar;

        ViewHolder(View itemView){
            super(itemView);
            ratingBar = itemView.findViewById(R.id.bookRating);
            bookName = itemView.findViewById(R.id.bookTitle);
            cover = itemView.findViewById(R.id.bookCover);
            authorName = itemView.findViewById(R.id.authorName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }
    public interface OnListItemClickListener
    {
        void onListItemClick(int clickedItemIndex);
    }
}
