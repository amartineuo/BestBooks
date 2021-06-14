package com.example.bestbooks.data.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bestbooks.data.models.Book;

import java.util.List;

@Dao
public interface BookDAO {

    @Query("SELECT * from books WHERE deleteBook = 0")
    LiveData<List<Book>> getAllBooks();

    @Query("SELECT * FROM books WHERE postID = :postID AND deleteBook = 0")
    LiveData<Book> getBookByID(int postID);

    @Query("SELECT * FROM books WHERE userID = :userID AND deleteBook = 0")
    LiveData<List<Book>> getUserBooks(int userID);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertBook(Book book);

    @Update
    int updateBook(Book book);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllBooks(List<Book> books);
}
