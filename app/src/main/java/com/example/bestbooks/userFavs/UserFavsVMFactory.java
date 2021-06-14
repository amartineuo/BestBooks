package com.example.bestbooks.userFavs;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bestbooks.data.repositories.FavoriteRepository;
import com.example.bestbooks.data.repositories.BookRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link FavoriteRepository}
 * {@link BookRepository}
 */
public class UserFavsVMFactory extends ViewModelProvider.NewInstanceFactory{

    private final BookRepository mBookRepository;
    private final FavoriteRepository mFavoriteRepository;

    public UserFavsVMFactory(BookRepository bookRepository, FavoriteRepository favoriteRepository) {
        this.mBookRepository = bookRepository;
        this.mFavoriteRepository = favoriteRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new UserFavsViewModel(mBookRepository, mFavoriteRepository);
    }
}
