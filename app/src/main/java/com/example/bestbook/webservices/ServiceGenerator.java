package com.example.bestbook.webservices;

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
