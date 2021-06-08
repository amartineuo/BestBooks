package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.bestbooks.interfaceAPI.MyJsonServerAPI;
import com.example.bestbooks.models.Book;
import com.example.bestbooks.models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrincipalActivity extends AppCompatActivity {

    private User myUser;
    List<Book> bookList = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


        //Inicializacion del recyclerView
        recyclerView = findViewById(R.id.recycler_books);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        //Informacion recibida del usuario registrado
        Bundle bundleRecibido = getIntent().getExtras();
        if(bundleRecibido != null){
            myUser = (User) bundleRecibido.getSerializable("myUser");
        }


        //Crear instancia de Retrofit y add el convertidor GSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/amartineuo/jsonBB/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyJsonServerAPI myJsonServerAPI = retrofit.create(MyJsonServerAPI.class);

        //Llamada a la API para obtener todos los libros
        Call<List<Book>> call = myJsonServerAPI.getAllBooks();

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                //Se obtiene la respuesta
                List<Book> books = response.body();

                if(books.size() == 0){
                    Log.d("Lista vacia", "No hay libros");
                }
                else {
                    for (Book book : books) {
                        bookList.add(book);
                        AdapterRecycler adapterRecycler = new AdapterRecycler(bookList);
                        recyclerView.setAdapter(adapterRecycler);
                        //Log.d("LIBRO correcto - ", book.getBookName());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("Inicio - NO SUCESSFUL", "onFailure");
            }
        });
    }
}