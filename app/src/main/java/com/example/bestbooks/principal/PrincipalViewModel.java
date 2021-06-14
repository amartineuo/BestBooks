package com.example.bestbooks.principal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.repositories.BookRepository;

import java.util.List;

public class PrincipalViewModel extends ViewModel {

    private final BookRepository mBookRepository;

    public PrincipalViewModel(BookRepository bookRepository){
        this.mBookRepository = bookRepository;
    }

    public LiveData<List<Book>> getAllCurrentBooks(){
        return mBookRepository.getAllCurrentBooks();
    }
}
