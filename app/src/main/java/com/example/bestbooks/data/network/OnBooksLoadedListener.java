package com.example.bestbooks.data.network;

import com.example.bestbooks.data.models.Book;

import java.util.List;

public interface OnBooksLoadedListener {

    void onBooksLoaded(List<Book> books);

}
