package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bestbooks.models.Book;
import com.example.bestbooks.models.Favorite;
import com.example.bestbooks.roomdb.ProjectDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserFavsActivity extends AppCompatActivity {

    private int myUserID;
    RecyclerView recyclerView;

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

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                ProjectDatabase db = ProjectDatabase.getInstance(UserFavsActivity.this);

                List<Book> bookFavFinal = new ArrayList<>();
                List<Favorite> myFavoritesList = db.getFavoriteDAO().getAllFavoritesByUser(myUserID);

                for (Favorite favorite : myFavoritesList){
                    bookFavFinal.add(db.getBookDAO().getBookByID(favorite.getBookFav()));
                }

                AdapterRecycler adapterRecycler = new AdapterRecycler(bookFavFinal, myUserID);
                runOnUiThread(() ->recyclerView.setAdapter(adapterRecycler));
            }
        });
    }
}