package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.bestbooks.models.User;

public class ProfileActivity extends AppCompatActivity {

    private User myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //Informacion recibida del usuario registrado
        Bundle bundleRecibido = getIntent().getExtras();
        if(bundleRecibido != null){
            myUser = (User) bundleRecibido.getSerializable("myUser");
        }


        TextView view_name_profile = findViewById(R.id.view_name_profile);
        view_name_profile.setText(myUser.getName());

        TextView view_age = findViewById(R.id.view_age);
        view_age.setText(String.valueOf(myUser.getEdad()));

        TextView view_username = findViewById(R.id.view_username);
        view_username.setText(myUser.getUsername());

        TextView view_email = findViewById(R.id.view_email);
        view_email.setText(myUser.getEmail());



    }
}