package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.models.Favorite;
import com.example.bestbooks.data.models.User;
import com.example.bestbooks.data.network.ProjectNetworkDataSource;
import com.example.bestbooks.data.repositories.BookRepository;
import com.example.bestbooks.data.repositories.FavoriteRepository;
import com.example.bestbooks.data.repositories.UserRepository;
import com.example.bestbooks.data.roomdb.ProjectDatabase;

public class DetailBookActivity extends AppCompatActivity {

    private int myUserID;
    private Book actualBook;

    ImageView imageView_favorite_no;
    ImageView imageView_favorite_yes;

    ImageView imageView_edit_book;
    ImageView imageView_delete_book;

    private BookRepository bookRepository;
    private UserRepository userRepository;
    private FavoriteRepository favoriteRepository;
    private boolean borrado = false;
    private boolean borradoFav = false;
    private boolean quieroBorrar = false;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);


        //Informacion del usuario registrado
        ClaseGlobal claseGlobal = (ClaseGlobal) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();

        //Informacion del libro sobre el que mostrar los detalles
        actualBook = claseGlobal.getBookAux();


        //----------INFORMACION DEL LIBRO-----------

        //Obtiene instancia del repository
        bookRepository = BookRepository.getInstance(ProjectDatabase.getInstance(this).getBookDAO(), ProjectNetworkDataSource.getInstance());

        //devuelve LiveData que podemos observar
        bookRepository.getBookByID(actualBook.getPostID()).observe(this, new Observer<Book>() {
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

        //Obtiene instancia del repository
        userRepository = UserRepository.getInstance(ProjectDatabase.getInstance(this).getUserDAO(), ProjectNetworkDataSource.getInstance());

        userRepository.getUserByID(actualBook.getUserID()).observe(this, new Observer<User>() {
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
                //finish();
                Toast.makeText(getApplicationContext(),"Reseña eliminada correctamente",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void deleteBook(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(!borrado) {
                    //Se actualiza el campo delete del book a 1 (borrado)
                    actualBook.setDeleteBook(1);
                    bookRepository.updateBook(actualBook);
                    borrado = true;

                    Intent intentDelete = new Intent(DetailBookActivity.this, PrincipalActivity.class);
                    startActivity(intentDelete);
                    finish();
                }
            }
        });
    }

    public void comprobarFav(){
        //Obtiene instancia del repository
        favoriteRepository = FavoriteRepository.getInstance(ProjectDatabase.getInstance(this).getFavoriteDAO(), ProjectNetworkDataSource.getInstance());

        favoriteRepository.getFavByUserAndBook(myUserID, actualBook.getPostID()).observe(this, new Observer<Favorite>() {
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

    //0 - add; 1 - delete
    public void ponerQuitarFav(int cod){

        if(cod == 0){ //add fav
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if(borradoFav) {
                        Favorite favAdd = new Favorite(myUserID, actualBook.getPostID(), 0);
                        favoriteRepository.insertFavorite(favAdd);

                        Log.d("Detalle", "Insertar Fav");
                        runOnUiThread(() -> {
                            imageView_favorite_yes.setVisibility(View.VISIBLE);
                            imageView_favorite_no.setVisibility(View.INVISIBLE);
                        });
                        borradoFav = false;
                    }
                }
            });

        }
        else{ //delete fav
            favoriteRepository.getFavByUserAndBook(myUserID, actualBook.getPostID()).observe(DetailBookActivity.this, new Observer<Favorite>() {
                @Override
                public void onChanged(Favorite favDelete) {

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (quieroBorrar) {
                                favDelete.setDeleteFav(1); //1 = borrado
                                favoriteRepository.updateFavorite(favDelete);

                                Log.d("Detalle", "Borrar Fav");
                                runOnUiThread(() -> {
                                    imageView_favorite_no.setVisibility(View.VISIBLE);
                                    imageView_favorite_yes.setVisibility(View.INVISIBLE);
                                });

                                quieroBorrar = false;
                            }
                        }
                    });

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