package com.example.bestbooks.data.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.models.Favorite;
import com.example.bestbooks.data.models.User;

@Database(entities = {User.class, Book.class, Favorite.class}, version = 1)
public abstract class ProjectDatabase extends RoomDatabase {

    public static ProjectDatabase instance;


    public static ProjectDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, ProjectDatabase.class, "BestBooks.db").build();
        }
        return instance;
    }

    public abstract UserDAO getUserDAO();

    public abstract BookDAO getBookDAO();

    public abstract FavoriteDAO getFavoriteDAO();
}
