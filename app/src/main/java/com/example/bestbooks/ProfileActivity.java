package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.bestbooks.models.User;
import com.example.bestbooks.roomdb.ProjectDatabase;

public class ProfileActivity extends AppCompatActivity {

    private int myUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //Informacion recibida del usuario registrado
        Bundle bundleRecibido = getIntent().getExtras();
        if(bundleRecibido != null){
            myUserID = bundleRecibido.getInt("myUserID");
        }

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ProjectDatabase db = ProjectDatabase.getInstance(ProfileActivity.this);

                //Usuario loggeado recuperado de la BD con el identificador
                User user = db.getUserDAO().getUserByID(myUserID);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView view_name_profile = findViewById(R.id.view_name_profile);
                        view_name_profile.setText(user.getName());

                        TextView view_age = findViewById(R.id.view_age);
                        view_age.setText(String.valueOf(user.getEdad()));

                        TextView view_username = findViewById(R.id.view_username);
                        view_username.setText(user.getUsername());

                        TextView view_email = findViewById(R.id.view_email);
                        view_email.setText(user.getEmail());
                    }
                });
            }
        });
    }
}