package com.example.bestbooks.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.SearchView;

import com.example.bestbooks.AppContainer;
import com.example.bestbooks.MyApplication;
import com.example.bestbooks.R;
import com.example.bestbooks.adapter.AdapterRecycler;
import com.example.bestbooks.data.models.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private androidx.appcompat.widget.SearchView searchView;
    RecyclerView recyclerView;
    private int myUserID;

    List<Book> bookList = new ArrayList<>();

    SearchViewModel searchVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_view_book);
        searchView.setOnQueryTextListener((androidx.appcompat.widget.SearchView.OnQueryTextListener) this);

        recyclerView = findViewById(R.id.recycler_search);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        //Informacion del usuario registrado
        MyApplication claseGlobal = (MyApplication) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();


        //Se crea una instancia de la clase contenedora  y el VM
        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        searchVM = new ViewModelProvider(this, appContainer.searchVMFactory).get(SearchViewModel.class);


        searchVM.getAllCurrentBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                bookList.clear();
                bookList.addAll(books);
                AdapterRecycler adapterRecycler = new AdapterRecycler(bookList, myUserID);
                recyclerView.setAdapter(adapterRecycler);
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

    //METODO PARA ORDENAR
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
        //Escucha cada vez que escribimos una letra en el searchview

        searchVM.getAllCurrentBooks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                bookList.clear();
                bookList.addAll(books);
                AdapterRecycler adapterRecycler = new AdapterRecycler(bookList, myUserID);
                recyclerView.setAdapter(adapterRecycler);
                adapterRecycler.filter(newText);
            }
        });
        return false;
    }
}