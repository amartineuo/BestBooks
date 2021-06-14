package com.example.bestbooks;

import android.app.Application;

import com.example.bestbooks.data.models.Book;

public class MyApplication extends Application {

    public AppContainer appContainer;

    private int myUserID;

    private Book bookAux;

    @Override
    public void onCreate() {
        super.onCreate();
        appContainer = new AppContainer(this);
    }

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
