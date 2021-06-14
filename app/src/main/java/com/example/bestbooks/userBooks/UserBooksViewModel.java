package com.example.bestbooks.userBooks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.repositories.BookRepository;

import java.util.List;

public class UserBooksViewModel extends ViewModel {

    private final BookRepository mBookRepository;

    public UserBooksViewModel(BookRepository bookRepository){
        this.mBookRepository = bookRepository;
    }

    //Obtener books actuales de un user
    public LiveData<List<Book>> getAllCurrentBooksByUser(int userID) {
        return mBookRepository.getAllCurrentBooksByUser(userID);
    }
}
