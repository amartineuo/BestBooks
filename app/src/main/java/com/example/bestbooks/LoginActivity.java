package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bestbooks.data.models.User;
import com.example.bestbooks.data.network.ProjectNetworkDataSource;
import com.example.bestbooks.data.repositories.UserRepository;
import com.example.bestbooks.data.roomdb.ProjectDatabase;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;

    private UserRepository userRepository;
    List<User> usersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText enter_email = findViewById(R.id.plain_enter_email);
        EditText enter_password = findViewById(R.id.plain_enter_password);

        Button button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myEmail = enter_email.getText().toString();
                String myPassword = enter_password.getText().toString();
                comprobarUsuario(myEmail, myPassword);
            }
        });

        TextView textView_register = findViewById(R.id.textView_register);
        textView_register.setOnClickListener(new View.OnClickListener() {
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


        //Obtiene instancia del repository
        userRepository = UserRepository.getInstance(ProjectDatabase.getInstance(this).getUserDAO(), ProjectNetworkDataSource.getInstance());

        userRepository.getUserByEmail(email).observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                usersList.clear();
                usersList.addAll(users);
                //No existe usuario registrado con ese email
                if(usersList.size() == 0 || usersList.get(0).getDeleteUser() == 1){
                    text_error_password.setVisibility(View.INVISIBLE);
                    text_error_email.setVisibility(View.VISIBLE);
                    return;
                }
                else { //Usuario si existe
                    text_error_email.setVisibility(View.INVISIBLE);
                    text_error_password.setVisibility(View.INVISIBLE);

                    for (User user : usersList) {
                        if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                            Log.d("Login correcto - ", user.getName());
                            text_error_password.setVisibility(View.INVISIBLE);
                            text_error_email.setVisibility(View.INVISIBLE);

                            //Iniciar la pagina principal una vez loggeado
                            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);

                            ClaseGlobal claseGlobal = (ClaseGlobal) getApplication().getApplicationContext();
                            claseGlobal.setMyUserID(user.getId());

                            startActivity(intent);
                            finish();
                        }
                        else {
                            Log.d("Login incorrecto -", "Password no coincide");
                            text_error_password.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
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