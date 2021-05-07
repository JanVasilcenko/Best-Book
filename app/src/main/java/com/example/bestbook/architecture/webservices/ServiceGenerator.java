package com.example.bestbook.architecture.webservices;

import com.example.bestbook.architecture.webservices.BookApi;

import retrofit2.Retrofit;

public class ServiceGenerator {
    private static BookApi bookApi;

    public static BookApi getBookApi()
    {
        if (bookApi == null)
        {
            bookApi = new Retrofit.Builder().baseUrl("https://openlibrary.org").build().create(BookApi.class);
        }
        return bookApi;
    }
}
