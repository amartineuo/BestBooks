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
import com.example.bestbooks.models.User;

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

        EditText enter_email = (EditText)findViewById(R.id.plain_enter_email);
        EditText enter_password = (EditText)findViewById(R.id.plain_enter_password);


        Button button_login = (Button)findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myEmail = enter_email.getText().toString();
                String myPassword = enter_password.getText().toString();
                comprobarUsuario(myEmail, myPassword);
            }
        });
    }

    private void comprobarUsuario(String email, String password){

        //Crear instancia de Retrofit y add el convertidor GSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/amartineuo/jsonBB/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyJsonServerAPI myJsonServerAPI = retrofit.create(MyJsonServerAPI.class);

        //Llamada a la API para obtener el usuario con ese email
        Call<List<User>> call = myJsonServerAPI.getUserByEmail(email);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                TextView text_error_password = (TextView) findViewById(R.id.text_user_no_password);
                TextView text_error_email = (TextView)findViewById(R.id.text_user_no_exit);

                //Se obtiene la respuesta
                List<User> users = response.body();

                //No existe usuario registrado con ese email
                if(users.size() == 0){
                    text_error_password.setVisibility(View.INVISIBLE);
                    text_error_email.setVisibility(View.VISIBLE);
                    return;
                }
                else { //Usuario si existe
                    for (User user : users) {
                        if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                            Log.d("Login correcto - ", user.getName());
                            text_error_password.setVisibility(View.INVISIBLE);

                            //Iniciar la pagina principal una vez loggeado
                            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("myUser", user);
                            intent.putExtras(bundle);

                            startActivity(intent);
                            finish();
                        }
                        else {
                            Log.d("Login incorrecto -", "Password no coincide");
                            text_error_password.setVisibility(View.VISIBLE);
                        }
                        text_error_email.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("Login - NO SUCESSFUL", "onFailure");
            }
        });
    }
}