package com.example.bestbooks.data.network;

import com.example.bestbooks.data.models.Favorite;

import java.util.List;

public interface OnFavoritesLoadedListener {

    void onFavoritesLoaded(List<Favorite> favorites);
}
