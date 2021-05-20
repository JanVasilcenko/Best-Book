package com.example.bestbook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestbook.R;
import com.example.bestbook.model.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    public ArrayList<Comment> comments;
    private Context context;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.comments = comments;
        this.context = context;
    }

    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_comment, parent,false);
        return new CommentAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(CommentAdapter.ViewHolder viewHolder, int position)
    {
        viewHolder.textView.setText(comments.get(position).getText());
        viewHolder.ratingBar.setRating(comments.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.commentText);
            ratingBar = itemView.findViewById(R.id.bookRating);
        }
    }
}
