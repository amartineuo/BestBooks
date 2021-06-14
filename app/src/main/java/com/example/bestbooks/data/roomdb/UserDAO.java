package com.example.bestbooks.data.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bestbooks.data.models.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * from users WHERE deleteUser = 0")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM users WHERE id = :id AND deleteUser = 0")
    LiveData<User> getUserByID(int id);

    @Query("SELECT * FROM users WHERE email = :email AND deleteUser = 0")
    LiveData<List<User>> getUserByEmail(String email);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertUser(User user);

    @Update
    int updateUser(User user);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllUsers(List<User> users);
}
