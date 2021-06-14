package com.example.bestbooks.data.network;

import com.example.bestbooks.AppExecutors;
import com.example.bestbooks.data.interfaceAPI.MyJsonServerAPI;
import com.example.bestbooks.data.models.Favorite;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoritesLoaderRunnable implements Runnable{

    private final OnFavoritesLoadedListener mOnFavoritesLoadedListener;

    public FavoritesLoaderRunnable(OnFavoritesLoadedListener onFavoritesLoadedListener){
        mOnFavoritesLoadedListener = onFavoritesLoadedListener;
    }

    @Override
    public void run() {
        //Cargar favoritos de la API

        //Crear instancia de Retrofit y add el convertidor GSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/amartineuo/jsonBB/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyJsonServerAPI myJsonServerAPI = retrofit.create(MyJsonServerAPI.class);

        try {
            List<Favorite> favorites = myJsonServerAPI.getAllFavorites().execute().body();
            //Llamar al listener con los datos obtenidos
            AppExecutors.getInstance().mainThread().execute(() -> mOnFavoritesLoadedListener.onFavoritesLoaded(favorites));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
