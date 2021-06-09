package com.example.bestbooks.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bestbooks.models.Book;

import java.util.List;

@Dao
public interface BookDAO {

    @Query("SELECT * from books")
    List<Book> getAllBooks();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void  insertBook(Book book);
}
