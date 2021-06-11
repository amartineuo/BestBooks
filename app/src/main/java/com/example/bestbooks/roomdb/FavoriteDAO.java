package com.example.bestbooks.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bestbooks.models.Favorite;
import com.example.bestbooks.models.User;

import java.util.List;

@Dao
public interface FavoriteDAO {

    @Query("SELECT * from favorites WHERE userFav = :userFav")
    List<Favorite> getAllFavoritesByUser(int userFav);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertFavorite(Favorite favorite);

    @Query("SELECT * from favorites WHERE userFav = :userFav AND bookFav = :bookFav")
    Favorite getFavByUserAndBook(int userFav, int bookFav);

    @Delete
    void deleteFavorite(Favorite favorite);

    @Query("DELETE FROM favorites WHERE  bookFav= :bookFav")
    void deleteFavoritesByBook(int bookFav);
}

