package com.example.bestbooks.detailBook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bestbooks.AppContainer;
import com.example.bestbooks.ImageLoadTask;
import com.example.bestbooks.login.LoginActivity;
import com.example.bestbooks.modifyBook.ModifyBookActivity;
import com.example.bestbooks.MyApplication;
import com.example.bestbooks.R;
import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.models.Favorite;
import com.example.bestbooks.data.models.User;
import com.example.bestbooks.principal.PrincipalActivity;
import com.example.bestbooks.profile.ProfileActivity;

public class DetailBookActivity extends AppCompatActivity {

    private int myUserID;
    private Book actualBook;

    ImageView imageView_favorite_no;
    ImageView imageView_favorite_yes;

    ImageView imageView_edit_book;
    ImageView imageView_delete_book;

    private boolean borrado = false;
    private boolean borradoFav = false;
    private boolean quieroBorrar = false;

    private  DetailBookViewModel detailBookVM;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);

        //Informacion del usuario registrado
        MyApplication claseGlobal = (MyApplication) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();

        //Informacion del libro sobre el que mostrar los detalles
        actualBook = claseGlobal.getBookAux();

        //Se crea una instancia de la clase contenedora  y el VM
        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        detailBookVM = new ViewModelProvider(this, appContainer.detailBookVMFactory).get(DetailBookViewModel.class);



        //----------INFORMACION DEL LIBRO-----------

        detailBookVM.getBookByID(actualBook.getPostID()).observe(this, new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                actualBook = book;

                if(!borrado) {
                    ImageView detail_book_img = findViewById(R.id.detail_book_img);
                    new ImageLoadTask(actualBook.getImg(), detail_book_img).execute();


                    TextView textView_book_name = findViewById(R.id.textView_book_name);
                    textView_book_name.setText(actualBook.getBookName());


                    TextView textView_author = findViewById(R.id.textView_author);
                    textView_author.setText(actualBook.getAuthor());


                    TextView textView_age = findViewById(R.id.textView_age);
                    textView_age.setText("(" + actualBook.getYear() + ")");


                    TextView textView_rating = findViewById(R.id.textView_rating);
                    if (actualBook.getRating() < 5) {
                        textView_rating.setTextColor(ContextCompat.getColor(DetailBookActivity.this, R.color.red));
                    } else {
                        textView_rating.setTextColor(ContextCompat.getColor(DetailBookActivity.this, R.color.green));
                    }
                    textView_rating.setText(String.valueOf(actualBook.getRating()));


                    TextView textView_recommend = findViewById(R.id.textView_recommend);
                    if (actualBook.getRecommend() == 0) {
                        textView_recommend.setText("Si");
                    } else {
                        textView_recommend.setText("No");
                    }

                    TextView textView_comentary = findViewById(R.id.textView_comentary);
                    textView_comentary.setText(actualBook.getCommentary());
                }
            }
        });



        //-------------------INFORMACION DEL USUARIO QUE LO PUBLICO-------------

        imageView_favorite_no = findViewById(R.id.imageView_favorite_no);
        imageView_favorite_yes = findViewById(R.id.imageView_favorite_yes);

        imageView_edit_book = findViewById(R.id.imageView_edit_book);
        imageView_delete_book = findViewById(R.id.imageView_delete_book);


        detailBookVM.getUserByID(actualBook.getUserID()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                TextView textView_name = findViewById(R.id.textView_name);
                textView_name.setText(user.getUsername());

                if (myUserID == user.getId()){ //Es una publicacion mia (puedo editar)

                    imageView_edit_book.setVisibility(View.VISIBLE);
                    imageView_delete_book.setVisibility(View.VISIBLE);
                    imageView_favorite_no.setVisibility(View.INVISIBLE);
                    imageView_favorite_yes.setVisibility(View.INVISIBLE);

                }
                else{ //No es una publicacion mia (puedo dar fav)
                    imageView_edit_book.setVisibility(View.INVISIBLE);
                    imageView_delete_book.setVisibility(View.INVISIBLE);
                }
            }
        });


        //Si no es mi book se comprueban los favoritos
        if(actualBook.getUserID() != myUserID){
            comprobarFav();
        }

        //No hay favorito (click para dar)
        imageView_favorite_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ponerQuitarFav(0);
            }
        });

        //Si hay favorito (click para quitar)
        imageView_favorite_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quieroBorrar = true;
                ponerQuitarFav(1);
            }
        });

        //Editar un book
        imageView_edit_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEdit = new Intent(DetailBookActivity.this, ModifyBookActivity.class);
                startActivity(intentEdit);
            }
        });

        //Eliminar un book
        imageView_delete_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook();
            }
        });
    }

    //BORRAR UN BOOK
    public void deleteBook(){
        if(!borrado) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailBookActivity.this);
            builder.setTitle("Eliminar reseña");
            builder.setMessage("¿Seguro que desea eliminar esta reseña? (Los datos se eliminarán de forma permanente)");

            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Se actualiza el campo delete del book a 1 (borrado)
                    actualBook.setDeleteBook(1);
                    detailBookVM.updateBook(actualBook);
                    borrado = true;

                    Intent intentDelete = new Intent(DetailBookActivity.this, PrincipalActivity.class);
                    startActivity(intentDelete);
                    finish();

                    Toast.makeText(getApplicationContext(),"Reseña eliminada correctamente",Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancelar", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    //COMPROBAR SI EL USUARIO HA DADO FAVORITO O NO
    public void comprobarFav(){
        detailBookVM.getFavByUserAndBook(myUserID, actualBook.getPostID()).observe(this, new Observer<Favorite>() {
            @Override
            public void onChanged(Favorite favorite) {

                if (favorite != null){ //Si hay fav
                    imageView_favorite_yes.setVisibility(View.VISIBLE);
                    imageView_favorite_no.setVisibility(View.INVISIBLE);
                    borradoFav = false;
                }
                else{ //No hay fav
                    imageView_favorite_no.setVisibility(View.VISIBLE);
                    imageView_favorite_yes.setVisibility(View.INVISIBLE);
                    borradoFav = true;
                }
            }
        });
    }

    //INSERTAR O ELIMINAR FAVORITO
    //0 - add; 1 - delete
    public void ponerQuitarFav(int cod){

        if(cod == 0){ //add fav
            if(borradoFav) {
                Favorite favAdd = new Favorite(myUserID, actualBook.getPostID(), 0);
                detailBookVM.insertFavorite(favAdd);

                Log.d("Detalle", "Insertar Fav");
                imageView_favorite_yes.setVisibility(View.VISIBLE);
                imageView_favorite_no.setVisibility(View.INVISIBLE);
                borradoFav = false;
            }
        }
        else{
            detailBookVM.getFavByUserAndBook(myUserID, actualBook.getPostID()).observe(this, new Observer<Favorite>() {
                @Override
                public void onChanged(Favorite favorite) {
                    if (quieroBorrar) {
                        favorite.setDeleteFav(1); //1 = borrado
                        detailBookVM.updateFavorite(favorite);

                        Log.d("Detalle", "Borrar Fav");
                        imageView_favorite_no.setVisibility(View.VISIBLE);
                        imageView_favorite_yes.setVisibility(View.INVISIBLE);
                        quieroBorrar = false;
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}