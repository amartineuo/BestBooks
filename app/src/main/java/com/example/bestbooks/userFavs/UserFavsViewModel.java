package com.example.bestbooks.userFavs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.models.Favorite;
import com.example.bestbooks.data.repositories.BookRepository;
import com.example.bestbooks.data.repositories.FavoriteRepository;

import java.util.List;

public class UserFavsViewModel extends ViewModel {

    private final BookRepository mBookRepository;
    private final FavoriteRepository mFavoriteRepository;


    public UserFavsViewModel(BookRepository bookRepository, FavoriteRepository favoriteRepository){
        this.mBookRepository = bookRepository;
        this.mFavoriteRepository = favoriteRepository;
    }

    //Obtener favoritos actuales de un usuario
    public LiveData<List<Favorite>> getAllCurrentFavoritesByUser(int userID) {
        return mFavoriteRepository.getAllCurrentFavoritesByUser(userID);
    }

    //Obtener book por su ID
    public LiveData<Book> getBookByID(int bookID) {
        return mBookRepository.getBookByID(bookID);
    }

}
