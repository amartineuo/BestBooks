package com.example.bestbooks.detailBook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.models.Favorite;
import com.example.bestbooks.data.models.User;
import com.example.bestbooks.data.repositories.BookRepository;
import com.example.bestbooks.data.repositories.FavoriteRepository;
import com.example.bestbooks.data.repositories.UserRepository;


public class DetailBookViewModel extends ViewModel {

    private final UserRepository mUserRepository;
    private final BookRepository mBookRepository;
    private final FavoriteRepository mFavoriteRepository;

    public DetailBookViewModel(UserRepository userRepository, BookRepository bookRepository, FavoriteRepository favoriteRepository){
        this.mUserRepository = userRepository;
        this.mBookRepository = bookRepository;
        this.mFavoriteRepository = favoriteRepository;
    }

    //Obtener book por su ID
    public LiveData<Book> getBookByID(int bookID) {
        return mBookRepository.getBookByID(bookID);
    }

    //Obtener usuario por su ID
    public LiveData<User> getUserByID(int userID) {
        return mUserRepository.getUserByID(userID);
    }

    //Actualizar book
    public void updateBook(Book book){
        mBookRepository.updateBook(book);
    }

    //Comprobar fav de usuario y un book
    public LiveData<Favorite> getFavByUserAndBook(int userID, int bookID) {
        return mFavoriteRepository.getFavByUserAndBook(userID, bookID);
    }

    //Insertar favorito
    public void insertFavorite(Favorite favorite) {
        mFavoriteRepository.insertFavorite(favorite);
    }

    //Modifica un favorito
    public void updateFavorite(Favorite favorite) {
        mFavoriteRepository.updateFavorite(favorite);
    }

    /*
    public LiveData<List<Book>> getAllCurrentBooks(){
        return mBookRepository.getAllCurrentBooks();
    }

     */
}
