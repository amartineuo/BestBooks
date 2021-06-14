package com.example.bestbooks.addBook;

import androidx.lifecycle.ViewModel;

import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.repositories.BookRepository;


public class AddBookViewModel extends ViewModel {

    private final BookRepository mBookRepository;

    public AddBookViewModel(BookRepository bookRepository){
        this.mBookRepository = bookRepository;
    }

    public void insertBook(Book book){
        mBookRepository.insertBook(book);
    }


}
