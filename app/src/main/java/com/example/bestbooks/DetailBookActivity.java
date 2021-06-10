package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bestbooks.models.Book;
import com.example.bestbooks.models.User;
import com.example.bestbooks.roomdb.ProjectDatabase;

import java.util.List;

public class DetailBookActivity extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);

        //Recibido del intent
        Book book;
        book = (Book) getIntent().getExtras().getSerializable("bookDetail");
        int myUserID = getIntent().getExtras().getInt("myUserID");


        //INFORMACION DEL USUARIO QUE LO PUBLICO

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                ProjectDatabase db = ProjectDatabase.getInstance(DetailBookActivity.this);

                User userCreatedBook = db.getUserDAO().getUserByID(myUserID);

                TextView textView_name = findViewById(R.id.textView_name);
                runOnUiThread(() ->textView_name.setText(userCreatedBook.getUsername()));
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
}