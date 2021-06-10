package com.example.bestbooks.roomdb;

import androidx.room.Dao;
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
    public User getUserByID (int id);

    @Query("SELECT * FROM users WHERE email = :email")
    public List<User> getUserByEmail(String email);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertUser(User user);

    @Update
    public int updateUser(User user);
}
