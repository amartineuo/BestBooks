package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.SearchView;

import com.example.bestbooks.adapter.AdapterRecycler;
import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.network.ProjectNetworkDataSource;
import com.example.bestbooks.data.repositories.BookRepository;
import com.example.bestbooks.data.roomdb.ProjectDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private androidx.appcompat.widget.SearchView searchView;
    RecyclerView recyclerView;
    private int myUserID;

    private BookRepository bookRepository;
    List<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_view_book);
        searchView.setOnQueryTextListener((androidx.appcompat.widget.SearchView.OnQueryTextListener) this);

        recyclerView = findViewById(R.id.recycler_search);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        //Informacion del usuario registrado
        ClaseGlobal claseGlobal = (ClaseGlobal) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();


        //Obtiene instancia del repository
        bookRepository = BookRepository.getInstance(ProjectDatabase.getInstance(this).getBookDAO(), ProjectNetworkDataSource.getInstance());

        //devuelve LiveData que podemos observar
        bookRepository.getAllCurrentBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) { //se llama automaticamente cada vez que los datos del liveData cambien
                bookList.clear();
                bookList.addAll(books);
                AdapterRecycler adapterRecycler = new AdapterRecycler(bookList, myUserID);
                runOnUiThread(() ->recyclerView.setAdapter(adapterRecycler));
            }
        });

        //ORDENAR DE MAYOR A MENOR
        Button button_rating_mayor = findViewById(R.id.button_rating_mayor);
        button_rating_mayor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordenar(0);
            }
        });

        //ORDENADOR DE MENOR A MAYOR
        Button button_rating_menor = findViewById(R.id.button_rating_menor);
        button_rating_menor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordenar(1);
            }
        });
    }

    //0 - de mayor a menor; 1 de menor a mayor
    public void ordenar(int ord){

        Collections.sort(bookList, new Comparator<Book>(){

            @Override
            public int compare(Book b1, Book b2) {
                if(ord == 0) {
                    return new Float(b2.getRating()).compareTo(new Float(b1.getRating()));
                }
                else{
                    return new Float(b1.getRating()).compareTo(new Float(b2.getRating()));
                }
            }
        });

        AdapterRecycler adapterRecycler = new AdapterRecycler(bookList, myUserID);
        runOnUiThread(() ->recyclerView.setAdapter(adapterRecycler));
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        //se ejecuta cuando presionemos enter o buscar
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //escucha cada vez que escribimos una letra en el searchview

        //devuelve LiveData que podemos observar
        bookRepository.getAllCurrentBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) { //se llama automaticamente cada vez que los datos del liveData cambien
                bookList.clear();
                bookList.addAll(books);
                AdapterRecycler adapterRecycler = new AdapterRecycler(bookList, myUserID);
                runOnUiThread(() ->recyclerView.setAdapter(adapterRecycler));
                adapterRecycler.filter(newText);
            }
        });
        return false;
    }
}