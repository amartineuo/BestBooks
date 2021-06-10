package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bestbooks.models.User;
import com.example.bestbooks.roomdb.ProjectDatabase;

public class ProfileActivity extends AppCompatActivity {

    private int myUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //Imagen usuario
        ImageView imageView = (ImageView)findViewById(R.id.image_profile_modify);
        imageView.setImageResource(R.drawable.ic_person);

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



        ImageView modify_profile = findViewById(R.id.modify_profile);
        modify_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, ModifyProfileActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("myUserID", myUserID);
                intent.putExtras(bundle);

                startActivity(intent);

            }
        });

        Button button_my_books = findViewById(R.id.button_my_books);
        button_my_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, UserBooksActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("myUserID", myUserID);
                intent.putExtras(bundle);

                startActivity(intent);

            }
        });

    }
}