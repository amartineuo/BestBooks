package com.example.bestbooks.userBooks;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bestbooks.data.repositories.BookRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link BookRepository}
 */
public class UserBooksVMFactory extends ViewModelProvider.NewInstanceFactory{

    private final BookRepository mBookRepository;

    public UserBooksVMFactory(BookRepository bookRepository) {
        this.mBookRepository = bookRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new UserBooksViewModel(mBookRepository);
    }
}
