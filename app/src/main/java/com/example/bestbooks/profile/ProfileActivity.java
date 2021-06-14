package com.example.bestbooks.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bestbooks.AppContainer;
import com.example.bestbooks.modifyProfile.ModifyProfileActivity;
import com.example.bestbooks.MyApplication;
import com.example.bestbooks.R;
import com.example.bestbooks.userBooks.UserBooksActivity;
import com.example.bestbooks.userFavs.UserFavsActivity;
import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.models.User;
import com.example.bestbooks.login.LoginActivity;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private int myUserID;

    private boolean borrado = false;

    private  ProfileViewModel profileVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //Imagen usuario
        ImageView imageView = (ImageView)findViewById(R.id.image_profile_modify);
        imageView.setImageResource(R.drawable.ic_person);


        //Informacion del usuario registrado
        MyApplication claseGlobal = (MyApplication) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();


        //Se crea una instancia de la clase contenedora  y el VM
        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        profileVM = new ViewModelProvider(this, appContainer.profileVMFactory).get(ProfileViewModel.class);


        profileVM.getUserByID(myUserID).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(!borrado) {
                    TextView view_name_profile = findViewById(R.id.view_name_profile);
                    view_name_profile.setText(user.getName());

                    TextView view_age = findViewById(R.id.view_age);
                    view_age.setText(String.valueOf(user.getEdad()));

                    TextView view_username = findViewById(R.id.view_username);
                    view_username.setText(user.getUsername());

                    TextView view_email = findViewById(R.id.view_email);
                    view_email.setText(user.getEmail());
                }
            }
        });



        //MODIFICAR PERFIL
        ImageView modify_profile = findViewById(R.id.modify_profile);
        modify_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ModifyProfileActivity.class);
                startActivity(intent);
            }
        });


        //ELIMINAR CUENTA
        ImageView delete_profile = (ImageView)findViewById(R.id.delete_profile);
        delete_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Eliminar cuenta de usuario");
                builder.setMessage("¿Seguro que desa eliminar su cuenta de usuario? (Los datos se eliminarán de forma permanente)");

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarCuenta();
                        Intent intentLogin = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intentLogin);
                        finish();
                        Toast.makeText(getApplicationContext(),"Su cuenta ha sido eliminada",Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        //MIS PUBLICACIONES
        Button button_my_books = findViewById(R.id.button_my_books);
        button_my_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UserBooksActivity.class);
                startActivity(intent);
            }
        });


        //MIS FAVORITOS
        Button button_my_favs = findViewById(R.id.button_my_favs);
        button_my_favs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UserFavsActivity.class);
                startActivity(intent);
            }
        });


        //CERRAR SESION
        Button button_logout = findViewById(R.id.button_logout);
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(),"¡Hasta la proxima!",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void eliminarCuenta(){

        if(!borrado) {

            profileVM.getUserByID(myUserID).observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    borrarBooksAndFavorites(user);
                    if(!borrado) {
                        borrado = true;
                        user.setDeleteUser(1); //1 = borrado
                        profileVM.updateUser(user);
                    }
                }
            });
        }
    }

    //Se borran los books y los favoritos a ese book del usuario a eliminar
    private void borrarBooksAndFavorites(User user){

        profileVM.getAllCurrentBooksByUser(myUserID).observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {

                for(Book book : books){
                    book.setDeleteBook(1); //1 = borrado

                    profileVM.updateBook(book);
                    profileVM.deleteFavoritesByBook(book.getPostID());
                }
            }
        });
    }
}