package com.example.bestbooks.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestbooks.data.models.User;
import com.example.bestbooks.data.repositories.UserRepository;

import java.util.List;


public class LoginViewModel extends ViewModel {

    private final UserRepository mUserRepository;

    public LoginViewModel(UserRepository userRepository){
        this.mUserRepository = userRepository;
    }

    public LiveData<List<User>> getUserByEmail(String emailUser) {
        return mUserRepository.getUserByEmail(emailUser);
    }

}
