package com.example.bestbooks.data.models;

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

    @ColumnInfo(name = "deleteFav")
    private int deleteFav; //0 no borrado; 1 borrado

    public Favorite(int userFav, int bookFav, int deleteFav) {
        this.userFav = userFav;
        this.bookFav = bookFav;
        this.deleteFav = deleteFav;
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

    public int getDeleteFav() {
        return deleteFav;
    }

    public void setDeleteFav(int deleteFav) {
        this.deleteFav = deleteFav;
    }
}
