package com.example.bestbooks.modifyBook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.repositories.BookRepository;


public class ModifyBookViewModel extends ViewModel {

    private final BookRepository mBookRepository;

    public ModifyBookViewModel(BookRepository bookRepository){
        this.mBookRepository = bookRepository;
    }

    //Obtener book por su ID
    public LiveData<Book> getBookByID(int bookID) {
        return mBookRepository.getBookByID(bookID);
    }

    //Actualizar book
    public void updateBook(Book book){
        mBookRepository.updateBook(book);
    }

}
