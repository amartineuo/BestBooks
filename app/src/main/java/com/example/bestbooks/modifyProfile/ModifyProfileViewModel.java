package com.example.bestbooks.modifyProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestbooks.data.models.User;
import com.example.bestbooks.data.repositories.UserRepository;


public class ModifyProfileViewModel extends ViewModel {

    private final UserRepository mUserRepository;

    public ModifyProfileViewModel(UserRepository userRepository){
        this.mUserRepository = userRepository;
    }

    //Obtener usuario por su ID
    public LiveData<User> getUserByID(int userID) {
        return mUserRepository.getUserByID(userID);
    }

    //Actualizar un usuario
    public void updateUser(User user){
        mUserRepository.updateUser(user);
    }

}
