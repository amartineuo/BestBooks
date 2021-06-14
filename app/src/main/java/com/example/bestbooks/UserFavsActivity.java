package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.bestbooks.adapter.AdapterRecycler;
import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.models.Favorite;
import com.example.bestbooks.data.network.ProjectNetworkDataSource;
import com.example.bestbooks.data.repositories.BookRepository;
import com.example.bestbooks.data.repositories.FavoriteRepository;
import com.example.bestbooks.data.roomdb.ProjectDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserFavsActivity extends AppCompatActivity {

    private int myUserID;
    RecyclerView recyclerView;

    private FavoriteRepository favoriteRepository;
    List<Book> favsUser = new ArrayList<>();
    List<Favorite> myFavoritesList = new ArrayList<>();

    private BookRepository bookRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_favs);

        //Informacion del usuario registrado
        ClaseGlobal claseGlobal = (ClaseGlobal) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();

        //Inicializacion del recyclerView
        recyclerView = findViewById(R.id.recycler_my_favs);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        //ObtIene instancia del repository
        favoriteRepository = FavoriteRepository.getInstance(ProjectDatabase.getInstance(this).getFavoriteDAO(), ProjectNetworkDataSource.getInstance());


        //ObtIene instancia del repository
        bookRepository = BookRepository.getInstance(ProjectDatabase.getInstance(this).getBookDAO(), ProjectNetworkDataSource.getInstance());


        favoriteRepository.getAllCurrentFavoritesByUser(myUserID).observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {
                favsUser.clear();
                myFavoritesList.clear();
                myFavoritesList.addAll(favorites);

                Log.d("SIZE", String.valueOf(favorites.size()));
                if(myFavoritesList.size() != 0) {
                    for (Favorite favorite : myFavoritesList) {
                        bookRepository.getBookByID(favorite.getBookFav()).observe(UserFavsActivity.this, new Observer<Book>() {
                            @Override
                            public void onChanged(Book book) {
                                if(book != null) {
                                    favsUser.add(book);
                                }
                                AdapterRecycler adapterRecycler = new AdapterRecycler(favsUser, myUserID);
                                runOnUiThread(() -> recyclerView.setAdapter(adapterRecycler));
                            }
                        });
                    }
                }
                else{
                    favsUser.clear();
                    AdapterRecycler adapterRecycler = new AdapterRecycler(favsUser, myUserID);
                    runOnUiThread(() -> recyclerView.setAdapter(adapterRecycler));
                }
            }
        });
    }
}