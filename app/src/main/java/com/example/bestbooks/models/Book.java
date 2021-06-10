package com.example.bestbooks.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "books")
public class Book implements Serializable {

    @PrimaryKey (autoGenerate = true)
    private int postID;

    @ColumnInfo(name = "userID")
    private int userID;

    @ColumnInfo(name = "rating")
    private float rating;

    @ColumnInfo(name = "bookName")
    private String bookName;

    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "year")
    private String year;

    @ColumnInfo(name = "commentary")
    private String commentary;

    @ColumnInfo(name = "img")
    private String img;

    @ColumnInfo(name = "recommend")
    private int recommend;

    public Book(int userID, float rating, String bookName, String author, String year, String commentary, String img, int recommend) {
        this.userID = userID;
        this.rating = rating;
        this.bookName = bookName;
        this.author = author;
        this.year = year;
        this.commentary = commentary;
        this.img = img;
        this.recommend = recommend;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
