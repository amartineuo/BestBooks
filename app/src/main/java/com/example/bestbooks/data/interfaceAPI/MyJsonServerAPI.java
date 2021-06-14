package com.example.bestbooks.data.interfaceAPI;

import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.models.Favorite;
import com.example.bestbooks.data.models.User;

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
