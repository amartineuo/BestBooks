package com.example.bestbooks.data.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bestbooks.data.models.Favorite;

import java.util.List;

@Dao
public interface FavoriteDAO {

    @Query("SELECT * from favorites WHERE userFav = :userFav AND deleteFav = 0")
    LiveData<List<Favorite>> getAllFavoritesByUser(int userFav);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertFavorite(Favorite favorite);

    @Query("SELECT * from favorites WHERE userFav = :userFav AND bookFav = :bookFav AND deleteFav = 0")
    LiveData<Favorite> getFavByUserAndBook(int userFav, int bookFav);

    @Delete
    void deleteFavorite(Favorite favorite);

    @Query("DELETE FROM favorites WHERE  bookFav= :bookFav")
    void deleteFavoritesByBook(int bookFav);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllFavorites(List<Favorite> favorites);

    @Update
    void updateFavorite(Favorite favorite);
}

