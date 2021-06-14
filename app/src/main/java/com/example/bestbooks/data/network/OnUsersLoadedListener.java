package com.example.bestbooks.data.network;

import com.example.bestbooks.data.models.User;

import java.util.List;

public interface OnUsersLoadedListener {

    void onUsersLoaded(List<User> users);
}
