package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bestbooks.models.Book;
import com.example.bestbooks.models.User;
import com.example.bestbooks.roomdb.ProjectDatabase;

import java.util.List;

public class DetailBookActivity extends AppCompatActivity {

    private int myUserID;
    private Book book;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);

        //Recibido del intent
        book = (Book) getIntent().getExtras().getSerializable("bookDetail");
        myUserID = getIntent().getExtras().getInt("myUserID");


        //INFORMACION DEL USUARIO QUE LO PUBLICO

        ImageView imageView_favorite_no = findViewById(R.id.imageView_favorite_no);
        ImageView imageView_favorite_yes = findViewById(R.id.imageView_favorite_yes);

        ImageView imageView_edit_book = findViewById(R.id.imageView_edit_book);
        ImageView imageView_delete_book = findViewById(R.id.imageView_delete_book);

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
                        imageView_favorite_no.setVisibility(View.VISIBLE);
                        imageView_favorite_yes.setVisibility(View.VISIBLE);
                        imageView_edit_book.setVisibility(View.INVISIBLE);
                        imageView_delete_book.setVisibility(View.INVISIBLE);
                    });
                }
            }
        });


        imageView_favorite_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"EN FAV",Toast.LENGTH_SHORT).show();
            }
        });

        //Editar un book
        imageView_edit_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEdit = new Intent(DetailBookActivity.this, ModifyBookActivity.class);

                Bundle bundleEdit = new Bundle();
                bundleEdit.putInt("myUserID", myUserID);
                bundleEdit.putSerializable("bookEdit", book);
                intentEdit.putExtras(bundleEdit);

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

                db.getBookDAO().deleteBook(book);

                Intent intentEdit = new Intent(DetailBookActivity.this, PrincipalActivity.class);

                Bundle bundleProfile = new Bundle();
                bundleProfile.putInt("myUserID", myUserID);
                intentEdit.putExtras(bundleProfile);

                startActivity(intentEdit);
            }
        });
    }
}