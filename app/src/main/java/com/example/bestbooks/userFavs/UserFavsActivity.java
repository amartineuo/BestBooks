package com.example.bestbooks.userFavs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bestbooks.AppContainer;
import com.example.bestbooks.MyApplication;
import com.example.bestbooks.R;
import com.example.bestbooks.adapter.AdapterRecycler;
import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.models.Favorite;


import java.util.ArrayList;
import java.util.List;

public class UserFavsActivity extends AppCompatActivity {

    private int myUserID;
    RecyclerView recyclerView;

    List<Book> favsUser = new ArrayList<>();
    List<Favorite> myFavoritesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_favs);


        //Informacion del usuario registrado
        MyApplication claseGlobal = (MyApplication) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();


        //Inicializacion del recyclerView
        recyclerView = findViewById(R.id.recycler_my_favs);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        //Se crea una instancia de la clase contenedora  y el VM
        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        UserFavsViewModel userFavsVM = new ViewModelProvider(this, appContainer.userFavsVMFactory).get(UserFavsViewModel.class);


        userFavsVM.getAllCurrentFavoritesByUser(myUserID).observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(List<Favorite> favorites) {

                favsUser.clear();
                myFavoritesList.clear();
                myFavoritesList.addAll(favorites);

                if(myFavoritesList.size() != 0) {
                    for (Favorite favorite : myFavoritesList) {

                        userFavsVM.getBookByID(favorite.getBookFav()).observe(UserFavsActivity.this, new Observer<Book>() {
                            @Override
                            public void onChanged(Book book) {
                                favsUser.add(book);
                                AdapterRecycler adapterRecycler = new AdapterRecycler(favsUser, myUserID);
                                recyclerView.setAdapter(adapterRecycler);
                            }
                        });
                    }
                }
                else{
                    favsUser.clear();
                    AdapterRecycler adapterRecycler = new AdapterRecycler(favsUser, myUserID);
                    recyclerView.setAdapter(adapterRecycler);
                }
            }
        });
    }
}