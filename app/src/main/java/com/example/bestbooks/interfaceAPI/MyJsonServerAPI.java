package com.example.bestbooks.interfaceAPI;

import com.example.bestbooks.models.Book;
import com.example.bestbooks.models.Favorite;
import com.example.bestbooks.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyJsonServerAPI {

    @GET("users")
    Call<List<User>> getAllUsers();

    @GET("books")
    Call<List<Book>> getAllBooks();

    @GET("favorites")
    Call<List<Favorite>> getAllFavorites();

}
