package com.example.bestbooks.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bestbooks.AppContainer;
import com.example.bestbooks.AppExecutors;
import com.example.bestbooks.MyApplication;
import com.example.bestbooks.R;
import com.example.bestbooks.data.models.User;
import com.example.bestbooks.principal.PrincipalActivity;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    List<User> usersList = new ArrayList<>();
    private boolean insert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Button button_register = findViewById(R.id.button_register);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name, username, email, password;
                int age;


                EditText reg_new_username = findViewById(R.id.reg_new_username);
                username = reg_new_username.getText().toString();

                EditText reg_new_age = findViewById(R.id.reg_new_age);
                age = Integer.valueOf(reg_new_age.getText().toString());

                EditText reg_new_name = findViewById(R.id.reg_new_name);
                name = reg_new_name.getText().toString();

                EditText reg_new_email = findViewById(R.id.reg_new_email);
                email = reg_new_email.getText().toString();

                EditText reg_new_password = findViewById(R.id.reg_new_password);
                password = reg_new_password.getText().toString();

                if (username.length() > 0 && name.length() > 0 && email.length() > 0 && password.length() > 0){
                    User newUser = new User(username, name, age, email, password, 0);
                    comprobarUsuario(newUser);
                }
            }
        });
    }

    private void comprobarUsuario(User newUser){

        TextView error_register = findViewById(R.id.error_register);


        //Se crea una instancia de la clase contenedora  y el VM
        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        RegisterViewModel registerVM = new ViewModelProvider(this, appContainer.registerVMFactory).get(RegisterViewModel.class);


        registerVM.getUserByEmail(newUser.getEmail()).observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                usersList.clear();
                usersList.addAll(users);

                error_register.setVisibility(View.INVISIBLE);

                //No existe usuario registrado con ese email
                if (usersList.size() == 0 && !insert) {
                    insert = true;

                    AppExecutors.getInstance().diskIO().execute(() -> {
                        runOnUiThread(() -> error_register.setVisibility(View.INVISIBLE));
                        int myUserID = (int) registerVM.insertUser(newUser);

                        //Registrar informacion del usuario loggeado
                        MyApplication claseGlobal = (MyApplication) getApplicationContext();
                        claseGlobal.setMyUserID(myUserID);

                        //Iniciar la pagina principal una vez loggeado
                        Intent intent = new Intent(RegisterActivity.this, PrincipalActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else { //Usuario si existe
                    if (!insert) {
                        error_register.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}