package com.example.bestbooks.modifyBook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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


public class ModifyBookActivity extends AppCompatActivity {

    private int myUserID;

    private Book bookEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_book);


        //Informacion del usuario registrado
        MyApplication claseGlobal = (MyApplication) getApplicationContext();
        myUserID = claseGlobal.getMyUserID();


        //Informacion del libro a modificar
        bookEdit = claseGlobal.getBookAux();


        //Se crea una instancia de la clase contenedora  y el VM
        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        ModifyBookViewModel modifyBookVM = new ViewModelProvider(this, appContainer.modifyBookVMFactory).get(ModifyBookViewModel.class);


        modifyBookVM.getBookByID(bookEdit.getPostID()).observe(this, new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                bookEdit = book;
            }
        });


        EditText edit_addBook_name = (EditText)findViewById(R.id.edit_addBook_name);
        edit_addBook_name.setText(bookEdit.getBookName());

        EditText edit_addBook_author = (EditText)findViewById(R.id.edit_addBook_author);
        edit_addBook_author.setText(bookEdit.getAuthor());

        EditText edit_addBook_year = (EditText)findViewById(R.id.edit_addBook_year);
        edit_addBook_year.setText(bookEdit.getYear());

        EditText edit_addBook_rating = (EditText)findViewById(R.id.edit_addBook_rating);
        edit_addBook_rating.setText(String.valueOf(bookEdit.getRating()));

        EditText edit_addBook_commentary = (EditText)findViewById(R.id.edit_addBook_commentary);
        edit_addBook_commentary.setText(bookEdit.getCommentary());

        EditText edit_addBook_img = (EditText)findViewById(R.id.edit_addBook_img);
        edit_addBook_img.setText(bookEdit.getImg());


        RadioButton radioButton_yes = findViewById(R.id.radioButton_yes);
        RadioButton radioButton_no = findViewById(R.id.radioButton_no);

        if(bookEdit.getRecommend() == 0){
            radioButton_yes.setChecked(true);
        }
        else{
            radioButton_no.setChecked(true);
        }

        //Aceptar modificacion
        Button button_modify_ok = (Button)findViewById(R.id.button_modify_ok);
        button_modify_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bookName, author, year, commentary, img;
                float rating;
                int recommend = 0;

                bookName = edit_addBook_name.getText().toString();
                author = edit_addBook_author.getText().toString();
                year = edit_addBook_year.getText().toString();
                rating = Float.valueOf(edit_addBook_rating.getText().toString());
                commentary = edit_addBook_commentary.getText().toString();
                img = edit_addBook_img.getText().toString();

                if(radioButton_yes.isChecked()){
                    recommend = 0;
                }
                if(radioButton_no.isChecked()){
                    recommend = 1;
                }

                if(bookName.length() > 0)
                    bookEdit.setBookName(bookName);

                if(author.length() > 0)
                    bookEdit.setAuthor(author);

                if(year.length() > 0)
                    bookEdit.setYear(year);

                if(commentary.length() > 0)
                    bookEdit.setCommentary(commentary);

                if(img.length() > 0)
                    bookEdit.setImg(img);

                bookEdit.setRating(rating);
                bookEdit.setRecommend(recommend);


                modifyBookVM.updateBook(bookEdit);

                finish();
                Toast.makeText(getApplicationContext(),"Cambios guardados",Toast.LENGTH_SHORT).show();
            }
        });

        //Cancelar modificacion
        Button button_modify_no = findViewById(R.id.button_modify_no);
        button_modify_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(getApplicationContext(),"Cambios cancelados",Toast.LENGTH_SHORT).show();
            }
        });
    }
}