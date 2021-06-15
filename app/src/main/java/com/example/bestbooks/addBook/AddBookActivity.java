package com.example.bestbooks.addBook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.bestbooks.AppContainer;
import com.example.bestbooks.MyApplication;
import com.example.bestbooks.R;
import com.example.bestbooks.data.models.Book;


public class AddBookActivity extends AppCompatActivity {

    private int myUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        //Informacion del usuario registrado
        MyApplication claseGlobal = (MyApplication) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();


        //Se crea una instancia de la clase contenedora  y el VM
        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        AddBookViewModel addBookVM = new ViewModelProvider(this, appContainer.addBookVMFactory).get(AddBookViewModel.class);


        //CREAR EL BOOK
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

                //Insertar el nuevo book
                addBookVM.insertBook(newBook);

                //Finalizar activity
                finish();
                Toast.makeText(getApplicationContext(),"Reseña creada correctamente",Toast.LENGTH_LONG).show();
            }
        });
    }
}