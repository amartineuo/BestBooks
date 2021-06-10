package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.bestbooks.interfaceAPI.MyJsonServerAPI;
import com.example.bestbooks.models.Book;
import com.example.bestbooks.models.User;
import com.example.bestbooks.roomdb.ProjectDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText enter_email = findViewById(R.id.plain_enter_email);
        EditText enter_password = findViewById(R.id.plain_enter_password);

        cargarDatosAPIaRoom();

        Button button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myEmail = enter_email.getText().toString();
                String myPassword = enter_password.getText().toString();
                comprobarUsuario(myEmail, myPassword);
            }
        });

        Button button_register_login = findViewById(R.id.button_register_login);
        button_register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void comprobarUsuario(String email, String password){

        TextView text_error_password = findViewById(R.id.text_user_no_password);
        TextView text_error_email = findViewById(R.id.text_user_no_exit);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ProjectDatabase db = ProjectDatabase.getInstance(LoginActivity.this);

                List<User> users = db.getUserDAO().getUserByEmail(email);

                //No existe usuario registrado con ese email
                if(users.size() == 0){
                    runOnUiThread(() ->text_error_password.setVisibility(View.INVISIBLE));
                    runOnUiThread(() ->text_error_email.setVisibility(View.VISIBLE));

                    return;
                }
                else { //Usuario si existe
                    for (User user : users) {
                        if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                            Log.d("Login correcto - ", user.getName());
                            runOnUiThread(() ->text_error_password.setVisibility(View.INVISIBLE));

                            //Iniciar la pagina principal una vez loggeado
                            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putInt("myUserID", user.getId());
                            intent.putExtras(bundle);

                            startActivity(intent);
                            finish();
                        }
                        else {
                            Log.d("Login incorrecto -", "Password no coincide");

                            runOnUiThread(() ->text_error_password.setVisibility(View.VISIBLE));
                        }
                        runOnUiThread(() ->text_error_email.setVisibility(View.INVISIBLE));
                    }
                }

            }
        });
    }

    private void cargarDatosAPIaRoom(){
        cargarBooksAPI();
        cargarUsersAPI();
    }

    private void cargarBooksAPI(){
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

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            ProjectDatabase db = ProjectDatabase.getInstance(LoginActivity.this);

                            for (Book book : books) {
                                db.getBookDAO().insertBook(book);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("Login - NO SUCESSFUL", "onFailure");
            }
        });
    }

    private void cargarUsersAPI(){
        //Crear instancia de Retrofit y add el convertidor GSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/amartineuo/jsonBB/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyJsonServerAPI myJsonServerAPI = retrofit.create(MyJsonServerAPI.class);

        //Llamada a la API para obtener todos los libros
        Call<List<User>> call = myJsonServerAPI.getAllUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                //Se obtiene la respuesta
                List<User> users = response.body();

                if(users.size() == 0){
                    Log.d("Lista vacia", "No hay usuarios");
                }
                else {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            ProjectDatabase db = ProjectDatabase.getInstance(LoginActivity.this);

                            for (User user : users) {
                                db.getUserDAO().insertUser(user);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("Login - NO SUCESSFUL", "onFailure");
            }
        });
    }
}