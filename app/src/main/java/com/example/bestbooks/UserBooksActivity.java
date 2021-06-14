package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bestbooks.adapter.AdapterRecycler;
import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.network.ProjectNetworkDataSource;
import com.example.bestbooks.data.repositories.BookRepository;
import com.example.bestbooks.data.roomdb.ProjectDatabase;
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

        //Informacion del usuario registrado
        ClaseGlobal claseGlobal = (ClaseGlobal) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();

        //Inicializacion del recyclerView
        recyclerView = findViewById(R.id.recycler_my_books);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        //ObtIene instancia del repository
        bookRepository = BookRepository.getInstance(ProjectDatabase.getInstance(this).getBookDAO(), ProjectNetworkDataSource.getInstance());


        //devuelve LiveData que podemos observar
        bookRepository.getAllCurrentBooksByUser(myUserID).observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) { //se llama automaticamente cada vez que los datos del liveData cambien
                booksUser.clear();
                booksUser.addAll(books);
                AdapterRecycler adapterRecycler = new AdapterRecycler(booksUser, myUserID);
                runOnUiThread(() ->recyclerView.setAdapter(adapterRecycler));
            }
        });


        //Button para add un book
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