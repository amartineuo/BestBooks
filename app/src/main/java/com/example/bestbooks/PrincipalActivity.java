package com.example.bestbooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.bestbooks.interfaceAPI.MyJsonServerAPI;
import com.example.bestbooks.models.Book;
import com.example.bestbooks.models.User;
import com.example.bestbooks.roomdb.ProjectDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrincipalActivity extends AppCompatActivity {

    private int myUserID;
    List<Book> bookList = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //Instancia de la base de datos
        ProjectDatabase.getInstance(this);

        //Inicializacion del recyclerView
        recyclerView = findViewById(R.id.recycler_books);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //Informacion recibida del usuario registrado
        Bundle bundleRecibido = getIntent().getExtras();
        if(bundleRecibido != null){
            myUserID = bundleRecibido.getInt("myUserID");
        }


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                ProjectDatabase db = ProjectDatabase.getInstance(PrincipalActivity.this);

                List<Book> books = db.getBookDAO().getAllBooks();

                for (Book book : books) {
                    bookList.add(book);
                }

                AdapterRecycler adapterRecycler = new AdapterRecycler(bookList, myUserID);
                runOnUiThread(() ->recyclerView.setAdapter(adapterRecycler));
            }
        });

        //Button para add un book
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, AddBookActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("myUserID", myUserID);
                intent.putExtras(bundle);

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

                Bundle bundleProfile = new Bundle();
                bundleProfile.putInt("myUserID", myUserID);
                intentProfile.putExtras(bundleProfile);

                startActivity(intentProfile);

                return true;

            case R.id.action_bar_search:
                Intent intentSearch = new Intent(this, SearchActivity.class);

                Bundle bundleSearch = new Bundle();
                bundleSearch.putInt("myUserID", myUserID);
                intentSearch.putExtras(bundleSearch);

                startActivity(intentSearch);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}