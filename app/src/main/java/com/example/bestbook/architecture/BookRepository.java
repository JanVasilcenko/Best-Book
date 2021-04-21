package com.example.bestbook.architecture;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.bestbook.model.Book;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {
    private static BookRepository instance;
    private final MutableLiveData<Book> book;
    private MutableLiveData<ArrayList<Book>> bookCollection;

    private BookRepository() {
        book = new MutableLiveData<>();
        bookCollection = new MutableLiveData<>();
    }

    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    public LiveData<Book> getBook() {
        return book;
    }

    public LiveData<ArrayList<Book>> getSearchedBooks() {
        return bookCollection;
    }

    public void searchForBooks(String name)
    {
        BookApi bookApi = ServiceGenerator.getBookApi();
        Call<ResponseBody> call = bookApi.getBooks(name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    try
                    {
                        JSONObject json = new JSONObject(response.body().string());
                        JSONArray docs = null;
                        if (response != null)
                        {
                            docs = json.getJSONArray("docs");

                            ArrayList<Book> extractedBooks = Book.fromJson(docs);
                            ArrayList<Book> books = new ArrayList<>();

                            if(extractedBooks.size() > 50)
                            {
                                books.clear();
                                for (int i = 0; i < 50; i++) {
                                 books.add(extractedBooks.get(i));
                                }
                            }
                            else
                                {
                                    books = new ArrayList<>(extractedBooks);
                                }

                            bookCollection.setValue(books);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    }
