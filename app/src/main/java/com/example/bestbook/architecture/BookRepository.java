package com.example.bestbook.architecture;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.bestbook.model.Book;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {
    private static BookRepository instance;
    private MutableLiveData<Book> book;
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

    public void getBookInfo(Book passedBook)
    {
        BookApi bookApi = ServiceGenerator.getBookApi();
        Call<ResponseBody> call = bookApi.getBookInfo(passedBook.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                   try
                   {
                       JSONObject json = new JSONObject(response.body().string());

                       if (json.has("publishers"))
                       {
                           JSONArray publishers = json.getJSONArray("publishers");
                           passedBook.setPublisher(publishers.getString(0));
                       }

                       if (json.has("number_of_pages"))
                       {
                           passedBook.setNumOfPages(Integer.toString(json.getInt("number_of_pages"))+" pages");
                       }

                       if (json.has("description"))
                       {
                           JSONObject descriptionObj = json.getJSONObject("description");
                           passedBook.setDescription(descriptionObj.getString("value"));
                       }

                       book.setValue(passedBook);
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
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
                                    if (extractedBooks.get(i).hasCoverImage())
                                    {
                                        books.add(extractedBooks.get(i));
                                    }
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
