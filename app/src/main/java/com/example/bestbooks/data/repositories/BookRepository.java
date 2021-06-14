package com.example.bestbooks.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.bestbooks.AppExecutors;
import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.network.ProjectNetworkDataSource;
import com.example.bestbooks.data.roomdb.BookDAO;

import java.util.Arrays;
import java.util.List;

public class BookRepository {
    private static final String LOG_TAG = BookRepository.class.getSimpleName();

    // For Singleton instantiation
    private static BookRepository sInstance;
    private final BookDAO mBookDao;
    private final ProjectNetworkDataSource mProjectNetworkDataSource;
    private final AppExecutors mExecutors = AppExecutors.getInstance();


    private BookRepository(BookDAO bookDao, ProjectNetworkDataSource projectNetworkDataSource) {
        mBookDao = bookDao;
        mProjectNetworkDataSource = projectNetworkDataSource;

        //LiveData que obtiene books de la red
        LiveData<Book[]> networkData = mProjectNetworkDataSource.getCurrentBooks();

        //Mientras que exista el repositorio, se observa la red con LiveData
        //Si el LiveData cambia se actualiza la base de datos
        networkData.observeForever(newBooksFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                //Insertar los nuevos books en la base de datos local
                mBookDao.insertAllBooks(Arrays.asList(newBooksFromNetwork));
                Log.d(LOG_TAG, "Nuevos valores de books insertados en Room");
            });
        });
    }

    public synchronized static BookRepository getInstance(BookDAO bookDao, ProjectNetworkDataSource projectNetworkDataSource) {
        Log.d(LOG_TAG, "Obteniendo el repositorio de books");
        if (sInstance == null) {
            sInstance = new BookRepository(bookDao, projectNetworkDataSource);
            Log.d(LOG_TAG, "Creado nuevo repositorio de books");
        }
        return sInstance;
    }

    /**
     * Operaciones realacionadas con BD
     **/

    //Obtener books actuales
    public LiveData<List<Book>> getAllCurrentBooks() {
        return mBookDao.getAllBooks();
    }

    //Obtener books actuales de un user
    public LiveData<List<Book>> getAllCurrentBooksByUser(int userID) {
        return mBookDao.getUserBooks(userID);
    }

    //Obtener book por su ID
    public LiveData<Book> getBookByID(int bookID) {
        return mBookDao.getBookByID(bookID);
    }

    //Insertar book
    public void insertBook(Book book){
        mBookDao.insertBook(book);
    }

    //Actualizar book
    public void updateBook(Book book){
        mBookDao.updateBook(book);
    }
}
