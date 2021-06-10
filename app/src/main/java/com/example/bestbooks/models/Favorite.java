package com.example.bestbooks.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Favorite {

    @PrimaryKey (autoGenerate = true)
    int id;

    @ColumnInfo(name = "userFav")
    int userFav;

    @ColumnInfo(name = "bookFav")
    int bookFav;

    public Favorite(int userFav, int bookFav) {
        this.userFav = userFav;
        this.bookFav = bookFav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserFav() {
        return userFav;
    }

    public void setUserFav(int userFav) {
        this.userFav = userFav;
    }

    public int getBookFav() {
        return bookFav;
    }

    public void setBookFav(int bookFav) {
        this.bookFav = bookFav;
    }
}
