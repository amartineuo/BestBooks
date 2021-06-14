package com.example.bestbooks.addBook;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bestbooks.data.repositories.BookRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link BookRepository}
 */
public class AddBookVMFactory extends ViewModelProvider.NewInstanceFactory{

    private final BookRepository mBookRepository;

    public AddBookVMFactory(BookRepository bookRepository) {
        this.mBookRepository = bookRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AddBookViewModel(mBookRepository);
    }
}
