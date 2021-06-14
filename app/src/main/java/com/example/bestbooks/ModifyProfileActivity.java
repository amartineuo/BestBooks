package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bestbooks.data.models.User;
import com.example.bestbooks.data.network.ProjectNetworkDataSource;
import com.example.bestbooks.data.repositories.UserRepository;
import com.example.bestbooks.data.roomdb.ProjectDatabase;

public class ModifyProfileActivity extends AppCompatActivity {

    private int myUserID;

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);

        //Imagen usuario
        ImageView imageView = (ImageView)findViewById(R.id.image_profile_modify);
        imageView.setImageResource(R.drawable.ic_person);


        //Informacion del usuario registrado
        ClaseGlobal claseGlobal = (ClaseGlobal) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();

        //Obtiene instancia del repository
        userRepository = UserRepository.getInstance(ProjectDatabase.getInstance(this).getUserDAO(), ProjectNetworkDataSource.getInstance());

        userRepository.getUserByID(myUserID).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                EditText new_name = findViewById(R.id.new_name);
                new_name.setText(user.getName());

                EditText new_username = findViewById(R.id.new_username);
                new_username.setText(user.getUsername());

                EditText new_age = findViewById(R.id.new_age);
                new_age.setText(String.valueOf(user.getEdad()));

                EditText new_email = findViewById(R.id.new_email);
                new_email.setText(user.getEmail());

                EditText last_password = findViewById(R.id.last_password);
                last_password.setText(user.getPassword());
            }
        });

        //Aceptar modificacion
        Button button_accept_edit = findViewById(R.id.button_accept_edit);
        button_accept_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userRepository.getUserByID(myUserID).observe(ModifyProfileActivity.this, new Observer<User>() {
                    @Override
                    public void onChanged(User newUser) {
                        String newName, newUsername, newEmail, lastPassword, newPassword, finalPassword;
                        int newAge;

                        EditText new_name = findViewById(R.id.new_name);
                        newName = new_name.getText().toString();

                        EditText new_username = findViewById(R.id.new_username);
                        newUsername = new_username.getText().toString();

                        EditText new_age = findViewById(R.id.new_age);
                        newAge = Integer.valueOf(new_age.getText().toString());

                        EditText new_email = findViewById(R.id.new_email);
                        newEmail = new_email.getText().toString();

                        EditText new_password = findViewById(R.id.new_password);
                        newPassword = new_password.getText().toString();

                        EditText last_password = findViewById(R.id.last_password);
                        lastPassword = last_password.getText().toString();

                        Log.d("lastPassword - ", lastPassword);
                        Log.d("newPassword - ", newPassword);

                        if(newPassword.length()!=0 && lastPassword.equals(newUser.getPassword())){
                            newUser.setName(newName);
                            newUser.setUsername(newUsername);
                            newUser.setEdad(newAge);
                            newUser.setEmail(newEmail);
                            newUser.setPassword(newPassword);
                        }
                        else{
                            newUser.setName(newName);
                            newUser.setUsername(newUsername);
                            newUser.setEdad(newAge);
                            newUser.setEmail(newEmail);
                            newUser.setPassword(lastPassword);
                        }

                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                userRepository.updateUser(newUser);
                            }
                        });
                        finish();
                    }
                });
                Toast.makeText(getApplicationContext(),"Cambios guardados",Toast.LENGTH_SHORT).show();
            }
        });

        //Cancelar modificacion
        Button button_cancel_edit = findViewById(R.id.button_cancel_edit);
        button_cancel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(getApplicationContext(),"Cambios cancelados",Toast.LENGTH_SHORT).show();
            }
        });
    }
}