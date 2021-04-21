package com.example.bestbook.architecture;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
