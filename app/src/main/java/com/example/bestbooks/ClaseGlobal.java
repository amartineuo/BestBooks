package com.example.bestbooks;

import android.app.Application;

import com.example.bestbooks.data.models.Book;

public class ClaseGlobal extends Application {

    private int myUserID;

    private Book bookAux;

    public int getMyUserID() {
        return myUserID;
    }

    public void setMyUserID(int myUserID) {
        this.myUserID = myUserID;
    }

    public Book getBookAux() {
        return bookAux;
    }

    public void setBookAux(Book bookAux) {
        this.bookAux = bookAux;
    }
}
