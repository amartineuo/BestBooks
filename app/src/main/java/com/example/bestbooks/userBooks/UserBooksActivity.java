package com.example.bestbooks.userBooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bestbooks.AppContainer;
import com.example.bestbooks.MyApplication;
import com.example.bestbooks.R;
import com.example.bestbooks.adapter.AdapterRecycler;
import com.example.bestbooks.addBook.AddBookActivity;
import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.repositories.BookRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UserBooksActivity extends AppCompatActivity {

    private int myUserID;
    RecyclerView recyclerView;

    private BookRepository bookRepository;
    private List<Book> booksUser = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);

        //Inicializacion del recyclerView
        recyclerView = findViewById(R.id.recycler_my_books);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //Informacion del usuario registrado
        MyApplication claseGlobal = (MyApplication) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();

        //Se crea una instancia de la clase contenedora  y el VM
        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        UserBooksViewModel userBooksVM = new ViewModelProvider(this, appContainer.userBooksVMFactory).get(UserBooksViewModel.class);


        userBooksVM.getAllCurrentBooksByUser(myUserID).observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                booksUser.clear();
                booksUser.addAll(books);
                AdapterRecycler adapterRecycler = new AdapterRecycler(booksUser, myUserID);
                runOnUiThread(() ->recyclerView.setAdapter(adapterRecycler));
            }
        });


        //BOTON PARA CREAR UN BOOK
        FloatingActionButton fab = findViewById(R.id.fabUserBooks);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserBooksActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });

    }
}