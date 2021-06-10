package com.example.bestbooks.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bestbooks.models.Book;

import java.util.List;

@Dao
public interface BookDAO {

    @Query("SELECT * from books")
    List<Book> getAllBooks();

    @Query("SELECT * FROM books WHERE postID = :postID")
    Book getBookByID(int postID);

    @Query("SELECT * FROM books WHERE userID = :userID")
    List<Book> getUserBooks(int userID);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void  insertBook(Book book);

    @Update
    int updateBook(Book book);

    @Delete
    void deleteBook(Book book);
}
