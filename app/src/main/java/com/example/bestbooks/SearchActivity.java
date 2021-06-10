package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.SearchView;

import com.example.bestbooks.models.Book;
import com.example.bestbooks.roomdb.ProjectDatabase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private androidx.appcompat.widget.SearchView searchView;
    RecyclerView recyclerView;
    private int myUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_view_book);
        searchView.setOnQueryTextListener((androidx.appcompat.widget.SearchView.OnQueryTextListener) this);

        recyclerView = findViewById(R.id.recycler_search);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        //Informacion recibida del usuario registrado
        Bundle bundleRecibido = getIntent().getExtras();
        if(bundleRecibido != null){
            myUserID = bundleRecibido.getInt("myUserID");
        }

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                ProjectDatabase db = ProjectDatabase.getInstance(SearchActivity.this);

                List<Book> bookList = db.getBookDAO().getAllBooks();

                AdapterRecycler adapterRecycler = new AdapterRecycler(bookList, myUserID);
                runOnUiThread(() ->recyclerView.setAdapter(adapterRecycler));
            }
        });

        Button button_rating_mayor = findViewById(R.id.button_rating_mayor);
        button_rating_mayor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordenar(0);
            }
        });

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
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                ProjectDatabase db = ProjectDatabase.getInstance(SearchActivity.this);

                List<Book> bookList = db.getBookDAO().getAllBooks();

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
        });
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        //se ejecuta cuando presionemos enter o buscar
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //escucha cada vez que escribimos una letra en el searchview

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                ProjectDatabase db = ProjectDatabase.getInstance(SearchActivity.this);

                List<Book> bookList = db.getBookDAO().getAllBooks();

                AdapterRecycler adapterRecycler = new AdapterRecycler(bookList, myUserID);
                runOnUiThread(() ->recyclerView.setAdapter(adapterRecycler));
                adapterRecycler.filter(newText);
            }
        });

        return false;
    }


}