package com.example.bestbooks.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bestbooks.models.Book;
import com.example.bestbooks.models.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * from users")
    List<User> getAllUsers();

    @Query("SELECT * FROM users WHERE id = :id")
    User getUserByID(int id);

    @Query("SELECT * FROM users WHERE email = :email")
    List<User> getUserByEmail(String email);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertUser(User user);

    @Update
    int updateUser(User user);

    @Delete
    void deleteUser(User user);
}
