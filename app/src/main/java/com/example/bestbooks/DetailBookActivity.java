package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bestbooks.models.Book;
import com.example.bestbooks.models.Favorite;
import com.example.bestbooks.models.User;
import com.example.bestbooks.roomdb.ProjectDatabase;

public class DetailBookActivity extends AppCompatActivity {

    private int myUserID;
    private Book book;

    ImageView imageView_favorite_no;
    ImageView imageView_favorite_yes;

    ImageView imageView_edit_book;
    ImageView imageView_delete_book;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);


        //Informacion del usuario registrado
        ClaseGlobal claseGlobal = (ClaseGlobal) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();

        //Informacion del libro sobre el que mostrar los detalles
        book = claseGlobal.getBookAux();


        //INFORMACION DEL USUARIO QUE LO PUBLICO

        imageView_favorite_no = findViewById(R.id.imageView_favorite_no);
        imageView_favorite_yes = findViewById(R.id.imageView_favorite_yes);

        imageView_edit_book = findViewById(R.id.imageView_edit_book);
        imageView_delete_book = findViewById(R.id.imageView_delete_book);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                ProjectDatabase db = ProjectDatabase.getInstance(DetailBookActivity.this);

                User userCreatedBook = db.getUserDAO().getUserByID(book.getUserID());

                TextView textView_name = findViewById(R.id.textView_name);
                runOnUiThread(() ->textView_name.setText(userCreatedBook.getUsername()));

                if (myUserID == userCreatedBook.getId()){ //Es una publicacion mia (puedo editar)

                    runOnUiThread(() -> {
                        imageView_edit_book.setVisibility(View.VISIBLE);
                        imageView_delete_book.setVisibility(View.VISIBLE);
                        imageView_favorite_no.setVisibility(View.INVISIBLE);
                        imageView_favorite_yes.setVisibility(View.INVISIBLE);
                    });

                }
                else{ //No es una publicacion mia (puedo dar fav)
                    runOnUiThread(() -> {
                        imageView_edit_book.setVisibility(View.INVISIBLE);
                        imageView_delete_book.setVisibility(View.INVISIBLE);
                    });

                }
            }
        });

        comprobarFav();

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
                ponerQuitarFav(1);
            }
        });

        //Editar un book
        imageView_edit_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEdit = new Intent(DetailBookActivity.this, ModifyBookActivity.class);

                /*
                Bundle bundleEdit = new Bundle();
                bundleEdit.putSerializable("bookEdit", book);
                intentEdit.putExtras(bundleEdit);
                 */

                //ClaseGlobal claseGlobal = (ClaseGlobal) getApplication().getApplicationContext();
                //claseGlobal.setBookAux(book);

                startActivity(intentEdit);
            }
        });

        //Eliminar un book
        imageView_delete_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook();
                finish();
                Toast.makeText(getApplicationContext(),"Reseña eliminada correctamente",Toast.LENGTH_SHORT).show();
            }
        });


        //INFORMACION DEL LIBRO

        ImageView detail_book_img = findViewById(R.id.detail_book_img);
        new ImageLoadTask(book.getImg(), detail_book_img).execute();


        TextView textView_book_name = findViewById(R.id.textView_book_name);
        textView_book_name.setText(book.getBookName());


        TextView textView_author = findViewById(R.id.textView_author);
        textView_author.setText(book.getAuthor());


        TextView textView_age = findViewById(R.id.textView_age);
        textView_age.setText("(" + book.getYear() + ")");


        TextView textView_rating = findViewById(R.id.textView_rating);
        if (book.getRating() < 5) {
            textView_rating.setTextColor(ContextCompat.getColor(this,R.color.red));
        }
        else{
            textView_rating.setTextColor(ContextCompat.getColor(this,R.color.green));
        }
        textView_rating.setText(String.valueOf(book.getRating()));


        TextView textView_recommend = findViewById(R.id.textView_recommend);
        if (book.getRecommend() == 0) {
            textView_recommend.setText("Si");
        }
        else{
            textView_recommend.setText("No");
        }

        TextView textView_comentary = findViewById(R.id.textView_comentary);
        textView_comentary.setText(book.getCommentary());

    }

    public void deleteBook(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ProjectDatabase db = ProjectDatabase.getInstance(DetailBookActivity.this);

                //Se borran los favoritos del book
                db.getFavoriteDAO().deleteFavoritesByBook(book.getPostID());

                //Se borra el book
                db.getBookDAO().deleteBook(book);

                Intent intentDelete = new Intent(DetailBookActivity.this, PrincipalActivity.class);
                startActivity(intentDelete);
                finish();
            }
        });
    }

    public void comprobarFav(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                ProjectDatabase db = ProjectDatabase.getInstance(DetailBookActivity.this);

                Favorite fav = db.getFavoriteDAO().getFavByUserAndBook(myUserID, book.getPostID());

                if (fav != null){ //Si hay fav
                    imageView_favorite_yes.setVisibility(View.VISIBLE);
                    imageView_favorite_no.setVisibility(View.INVISIBLE);
                }
                else{ //No hay fav
                    imageView_favorite_no.setVisibility(View.VISIBLE);
                    imageView_favorite_yes.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    //0 - add; 1 - delete
    public void ponerQuitarFav(int cod){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                ProjectDatabase db = ProjectDatabase.getInstance(DetailBookActivity.this);

                if(cod == 0){ //add fav
                    Favorite favAdd = new Favorite(myUserID, book.getPostID());
                    db.getFavoriteDAO().insertFavorite(favAdd);
                    Log.d("Detalle", "Insertar Fav");
                    runOnUiThread(() -> {
                        imageView_favorite_yes.setVisibility(View.VISIBLE);
                        imageView_favorite_no.setVisibility(View.INVISIBLE);
                    });
                }
                else{ //delete fav
                    Favorite favDelete = db.getFavoriteDAO().getFavByUserAndBook(myUserID, book.getPostID());
                    db.getFavoriteDAO().deleteFavorite(favDelete);
                    Log.d("Detalle", "Borrar Fav");
                    runOnUiThread(() -> {
                        imageView_favorite_no.setVisibility(View.VISIBLE);
                        imageView_favorite_yes.setVisibility(View.INVISIBLE);
                    });
                }
            }
        });
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