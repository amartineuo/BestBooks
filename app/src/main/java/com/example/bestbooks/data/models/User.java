package com.example.bestbooks.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users")
public class User implements Serializable {

    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "edad")
    private int edad;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "deleteUser")
    private int deleteUser; //0 no borrado; 1 borrado

    public User(String username, String name, int edad, String email, String password, int deleteUser) {
        this.username = username;
        this.name = name;
        this.edad = edad;
        this.email = email;
        this.password = password;
        this.deleteUser = deleteUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(int deleteUser) {
        this.deleteUser = deleteUser;
    }
}
