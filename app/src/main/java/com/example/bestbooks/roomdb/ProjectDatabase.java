package com.example.bestbooks.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bestbooks.models.Book;
import com.example.bestbooks.models.User;

@Database(entities = {User.class, Book.class}, version = 1)
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
}
