package com.example.bestbooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.bestbooks.adapter.AdapterRecycler;
import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.network.ProjectNetworkDataSource;
import com.example.bestbooks.data.repositories.BookRepository;
import com.example.bestbooks.data.roomdb.ProjectDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private int myUserID;
    List<Book> bookList = new ArrayList<>();
    RecyclerView recyclerView;

    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;

    private BookRepository bookRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //Inicializacion del recyclerView
        recyclerView = findViewById(R.id.recycler_books);
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


        //Button para add un book
        FloatingActionButton fab = findViewById(R.id.fabPrincipal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_bar_profile:
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                startActivity(intentProfile);
                return true;

            case R.id.action_bar_search:
                Intent intentSearch = new Intent(this, SearchActivity.class);
                startActivity(intentSearch);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            moveTaskToBack(true);
        }else {
            Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }
}