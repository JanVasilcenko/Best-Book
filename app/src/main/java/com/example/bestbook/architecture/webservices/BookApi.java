package com.example.bestbook.architecture.webservices;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookApi {

    @GET("/search.json")
    Call<ResponseBody> getBooks(@Query("q") String q);

    @GET("books/{id}.json")
    Call<ResponseBody> getBookInfo(@Path("id") String id);


}
