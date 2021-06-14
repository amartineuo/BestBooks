package com.example.bestbooks.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bestbooks.data.repositories.UserRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link UserRepository}
 */
public class LoginVMFactory extends ViewModelProvider.NewInstanceFactory{

    private final UserRepository mUserRepository;

    public LoginVMFactory(UserRepository userRepository) {
        this.mUserRepository = userRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new LoginViewModel(mUserRepository);
    }
}
