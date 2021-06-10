package com.example.bestbooks;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestbooks.models.Book;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.ViewHolderRecycler> {

    List<Book> bookList;
    List<Book> originalBookList;

    public AdapterRecycler(List<Book> bookList) {

        this.bookList = bookList;
        this.originalBookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolderRecycler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Enlace el adapter con el archivo del item_grid_books

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_books, null, false);
        return new ViewHolderRecycler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRecycler holder, int position) {

        //Comunica el adaptador y el viewHolder
        try {
            holder.asignarBooks(bookList.get(position));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        //Devuelve el tamano de la lista de books
        return bookList.size();
    }

    public class ViewHolderRecycler extends RecyclerView.ViewHolder {

        ImageView image_adapter_grid;
        TextView text_adpter_grid;


        public ViewHolderRecycler(@NonNull View itemView) {
            super(itemView);

            image_adapter_grid = itemView.findViewById(R.id.image_adapter_grid);
            text_adpter_grid = itemView.findViewById(R.id.text_adpter_grid);


        }


        public void asignarBooks(Book book) throws IOException {
            //Establecer imagen
            new ImageLoadTask(book.getImg().toString(), image_adapter_grid).execute();

            //Establecer titulo del libro
            text_adpter_grid.setText(book.getBookName());

        }
    }

    public void filter(String search){
        if (search.length() == 0){
            //bookList.clear();
            originalBookList.addAll(bookList);
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<Book> list_search = bookList.stream().filter(book -> book.getBookName().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toList());
                bookList.clear();
                bookList.addAll(list_search);
            }
            else{
                bookList.clear();
                for (Book book : originalBookList){
                    if(book.getBookName().toLowerCase().contains(search.toLowerCase())){
                        bookList.add(book);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
