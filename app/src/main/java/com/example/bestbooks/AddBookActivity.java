package com.example.bestbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.network.ProjectNetworkDataSource;
import com.example.bestbooks.data.repositories.BookRepository;
import com.example.bestbooks.data.roomdb.ProjectDatabase;


public class AddBookActivity extends AppCompatActivity {

    private int myUserID;

    private BookRepository bookRepository;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);


        //Informacion del usuario registrado
        ClaseGlobal claseGlobal = (com.example.bestbooks.ClaseGlobal) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();


        Button button_add_book = (Button)findViewById(R.id.button_add_book);
        button_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bookName, author, year, commentary, img;
                float rating;
                int recommend = 0;

                EditText edit_addBook_name = (EditText)findViewById(R.id.edit_addBook_name);
                bookName = edit_addBook_name.getText().toString();

                EditText edit_addBook_author = (EditText)findViewById(R.id.edit_addBook_author);
                author = edit_addBook_author.getText().toString();

                EditText edit_addBook_year = (EditText)findViewById(R.id.edit_addBook_year);
                year = edit_addBook_year.getText().toString();

                EditText edit_addBook_rating = (EditText)findViewById(R.id.edit_addBook_rating);
                rating = Float.valueOf(edit_addBook_rating.getText().toString());


                EditText edit_addBook_commentary = (EditText)findViewById(R.id.edit_addBook_commentary);
                commentary = edit_addBook_commentary.getText().toString();


                EditText edit_addBook_img = (EditText)findViewById(R.id.edit_addBook_img);
                img = edit_addBook_img.getText().toString();

                RadioButton radioButton_yes = findViewById(R.id.radioButton_yes);
                RadioButton radioButton_no = findViewById(R.id.radioButton_no);
                
                if(radioButton_yes.isChecked()){
                    recommend = 0;
                }
                else{
                    if(radioButton_no.isChecked()){
                        recommend = 1;
                    }
                }

                Book newBook = new Book(myUserID, rating, bookName, author, year, commentary, img, recommend, 0);

                //Obtiene instancia del repository
                bookRepository = BookRepository.getInstance(ProjectDatabase.getInstance(AddBookActivity.this).getBookDAO(), ProjectNetworkDataSource.getInstance());

                //Insertar book
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        bookRepository.insertBook(newBook);
                    }
                });

                finish();
            }
        });
    }
}