package com.example.bestbooks.data.network;

import com.example.bestbooks.AppExecutors;
import com.example.bestbooks.data.interfaceAPI.MyJsonServerAPI;
import com.example.bestbooks.data.models.User;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersLoaderRunnable implements Runnable{

    private final OnUsersLoadedListener mOnUsersLoadedListener;

    public UsersLoaderRunnable(OnUsersLoadedListener onUsersLoadedListener){
        mOnUsersLoadedListener = onUsersLoadedListener;
    }

    @Override
    public void run() {
        //Cargar usuarios de la BD remota

        //Crear instancia de Retrofit y add el convertidor GSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/amartineuo/jsonBB/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyJsonServerAPI myJsonServerAPI = retrofit.create(MyJsonServerAPI.class);

        try {
            List<User> users = myJsonServerAPI.getAllUsers().execute().body();
            //Llamar al listener con los datos obtenidos
            AppExecutors.getInstance().mainThread().execute(() -> mOnUsersLoadedListener.onUsersLoaded(users));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
