package com.example.bestbooks.profile;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bestbooks.data.repositories.BookRepository;
import com.example.bestbooks.data.repositories.FavoriteRepository;
import com.example.bestbooks.data.repositories.UserRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link UserRepository}
 * {@link BookRepository}
 * {@link FavoriteRepository}
 */
public class ProfileVMFactory extends ViewModelProvider.NewInstanceFactory{

    private final UserRepository mUserRepository;
    private final BookRepository mBookRepository;
    private final FavoriteRepository mFavoriteRepository;

    public ProfileVMFactory(UserRepository userRepository, BookRepository bookRepository, FavoriteRepository favoriteRepository) {
        this.mUserRepository = userRepository;
        this.mBookRepository = bookRepository;
        this.mFavoriteRepository = favoriteRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ProfileViewModel(mUserRepository, mBookRepository, mFavoriteRepository);
    }
}
