package com.example.bestbooks.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.models.User;
import com.example.bestbooks.data.repositories.BookRepository;
import com.example.bestbooks.data.repositories.FavoriteRepository;
import com.example.bestbooks.data.repositories.UserRepository;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private final UserRepository mUserRepository;
    private final BookRepository mBookRepository;
    private final FavoriteRepository mFavoriteRepository;

    public ProfileViewModel(UserRepository userRepository, BookRepository bookRepository, FavoriteRepository favoriteRepository){
        this.mUserRepository = userRepository;
        this.mBookRepository = bookRepository;
        this.mFavoriteRepository = favoriteRepository;
    }

    //Obtener usuario por su ID
    public LiveData<User> getUserByID(int userID) {
        return mUserRepository.getUserByID(userID);
    }

    //Actualizar un usuario
    public void updateUser(User user){
        mUserRepository.updateUser(user);
    }

    //Obtener books actuales de un user
    public LiveData<List<Book>> getAllCurrentBooksByUser(int userID) {
        return mBookRepository.getAllCurrentBooksByUser(userID);
    }

    //Actualizar book
    public void updateBook(Book book){
        mBookRepository.updateBook(book);
    }

    //Elimina los favoritos de un book
    public void deleteFavoritesByBook(int bookID) {
        mFavoriteRepository.deleteFavoritesByBook(bookID);
    }
}
