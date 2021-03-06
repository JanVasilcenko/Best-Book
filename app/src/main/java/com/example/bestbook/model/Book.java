package com.example.bestbook.model;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    private String id;
    private String title;
    private String author;
    private String publisher;
    private String numOfPages;
    private String description;
    private boolean hasCoverImage;
    private float rating;

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean hasCoverImage() {
        return hasCoverImage;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setNumOfPages(String numOfPages) {
        this.numOfPages = numOfPages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getNumOfPages() {
        return numOfPages;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getMediumCoverUrl()
    {
        return "https://covers.openlibrary.org/b/olid/" + id + "-M.jpg?default=false";
    }

    public String getLargeCoverUrl()
    {
        return "https://covers.openlibrary.org/b/olid/" + id + "-L.jpg?default=false";
    }
    public static Book fromJsonDetailInfo(JSONObject json)
    {
        Book myBook = new Book();

        try
        {
            if (json.has("title"))
            {
                myBook.setTitle(json.getString("title"));
            }

            if (json.has("publishers"))
            {
                JSONArray publishers = json.getJSONArray("publishers");
                myBook.setPublisher(publishers.getString(0));
            }

            if (json.has("number_of_pages"))
            {
                myBook.setNumOfPages(Integer.toString(json.getInt("number_of_pages"))+" pages");
            }

            if (json.has("description"))
            {
                JSONObject descriptionObj;
                try
                {
                    descriptionObj = json.getJSONObject("description");
                    myBook.setDescription(descriptionObj.getString("value"));
                }
                catch (JSONException e)
                {
                    myBook.setDescription(json.getString("description"));
                }


            }
            if (json.has("author_name"))
            {
                try
                {
                    final JSONArray authors = json.getJSONArray("author_name");
                    int numAuthors = authors.length();
                    final String[] authorStrings = new String[numAuthors];
                    for (int i = 0; i < numAuthors; i++) {
                        authorStrings[i] = authors.getString(i);
                    }
                    myBook.setAuthor(TextUtils.join(", ",authorStrings));
                }catch (JSONException e)
                {
                    myBook.setAuthor("");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return myBook;
    }

    public static Book fromJsonBasicInfo(JSONObject jsonObject)
    {
        Book book = new Book();
        try
        {
            if(jsonObject.has("cover_edition_key"))
            {
                book.id = jsonObject.getString("cover_edition_key");
            }
            else if(jsonObject.has("edition_key"))
            {
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                book.id = ids.getString(0);
            }

            if (jsonObject.has("cover_i"))
            {
                book.hasCoverImage = true;
            }

            book.title = jsonObject.has("title_suggest") ? jsonObject.getString("title_suggest") : "";
            book.author = getAuthor(jsonObject);
        } catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
        return book;
    }

    public static String getAuthor(final JSONObject jsonObject)
    {
        try
        {
            final JSONArray authors = jsonObject.getJSONArray("author_name");
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];
            for (int i = 0; i < numAuthors; i++) {
                authorStrings[i] = authors.getString(i);
            }
            return TextUtils.join(", ",authorStrings);
        }catch (JSONException e)
        {
            return "";
        }
    }

    public static ArrayList<Book> fromJson(JSONArray jsonArray)
    {
        ArrayList<Book> books = new ArrayList<Book>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson = null;
            try
            {
                bookJson = jsonArray.getJSONObject(i);
            }catch (Exception e)
            {
                e.printStackTrace();
                continue;
            }
            Book book = Book.fromJsonBasicInfo(bookJson);
            if (book != null)
            {
                books.add(book);
            }
        }
        return books;
    }

}

