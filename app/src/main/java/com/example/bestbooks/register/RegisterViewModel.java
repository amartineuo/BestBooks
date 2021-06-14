package com.example.bestbooks.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestbooks.data.models.User;
import com.example.bestbooks.data.repositories.UserRepository;

import java.util.List;

public class RegisterViewModel extends ViewModel {

    private final UserRepository mUserRepository;

    public RegisterViewModel(UserRepository userRepository){
        this.mUserRepository = userRepository;
    }

    public LiveData<List<User>> getUserByEmail(String emailUser) {
        return mUserRepository.getUserByEmail(emailUser);
    }

    //Inserta un usuario
    public long insertUser(User user){
        return mUserRepository.insertUser(user);
    }
}
