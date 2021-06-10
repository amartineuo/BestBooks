package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bestbooks.models.Book;
import com.example.bestbooks.roomdb.ProjectDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserBooksActivity extends AppCompatActivity {

    private int myUserID;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);

        //Informacion recibida del usuario registrado
        Bundle bundleRecibido = getIntent().getExtras();
        if(bundleRecibido != null){
            myUserID = bundleRecibido.getInt("myUserID");
        }

        //Inicializacion del recyclerView
        recyclerView = findViewById(R.id.recycler_my_books);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                ProjectDatabase db = ProjectDatabase.getInstance(UserBooksActivity.this);

                List<Book> myBookList = db.getBookDAO().getUserBooks(myUserID);

                AdapterRecycler adapterRecycler = new AdapterRecycler(myBookList, myUserID);
                runOnUiThread(() ->recyclerView.setAdapter(adapterRecycler));
            }
        });

    }
}