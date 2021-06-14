package com.example.bestbooks.data.network;

import com.example.bestbooks.AppExecutors;
import com.example.bestbooks.data.interfaceAPI.MyJsonServerAPI;
import com.example.bestbooks.data.models.Book;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BooksLoaderRunnable implements Runnable{

    private final OnBooksLoadedListener mOnBooksLoadedListener;

    public BooksLoaderRunnable(OnBooksLoadedListener onBooksLoadedListener){
        mOnBooksLoadedListener = onBooksLoadedListener;
    }

    @Override
    public void run() {
        //Cargar books de la API

        //Crear instancia de Retrofit y add el convertidor GSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/amartineuo/jsonBB/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyJsonServerAPI myJsonServerAPI = retrofit.create(MyJsonServerAPI.class);

        try {
            List<Book> books = myJsonServerAPI.getAllBooks().execute().body();
            //Llamar al listener con los datos obtenidos
            AppExecutors.getInstance().mainThread().execute(() -> mOnBooksLoadedListener.onBooksLoaded(books));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
