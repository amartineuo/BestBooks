package com.example.bestbooks.data.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bestbooks.AppExecutors;
import com.example.bestbooks.data.models.Book;
import com.example.bestbooks.data.models.Favorite;
import com.example.bestbooks.data.models.User;

import java.util.List;

public class ProjectNetworkDataSource {

    private static final String LOG_TAG = ProjectNetworkDataSource.class.getSimpleName();
    private static ProjectNetworkDataSource sInstance;

    //LiveData que almacena los ultimos books descargados
    private final MutableLiveData<Book[]> mDownloadedBooks;

    //LiveData que almacena los ultimos usuarios descargados
    private final MutableLiveData<User[]> mDownloadedUsers;

    //LiveData que almacena los ultimos favoritos descargados
    private final MutableLiveData<Favorite[]> mDownloadedFavorites;

    private ProjectNetworkDataSource() {
        mDownloadedBooks = new MutableLiveData<>();
        fetchBooks(); //Cuando se crea la fuente de datos se inicializa con los books

        mDownloadedUsers = new MutableLiveData<>();
        fetchUsers(); //Cuando se crea la fuente de datos se inicializa con los usuarios

        mDownloadedFavorites = new MutableLiveData<>();
        fetchFavorites(); //Cuando se crea la fuente de datos se inicializa con los favoritos
    }

    public synchronized static ProjectNetworkDataSource getInstance() {
        Log.d(LOG_TAG, "Obteniendo la fuente de datos de la red");
        if (sInstance == null) {
            sInstance = new ProjectNetworkDataSource();
            Log.d(LOG_TAG, "Nueva fuente de datos de red creada");
        }
        return sInstance;
    }

    //Obtener los libros actuales
    public LiveData<Book[]> getCurrentBooks() {
        return mDownloadedBooks;
    }

    //Obtener los usuarios actuales
    public LiveData<User[]> getCurrentUsers() {
        return mDownloadedUsers;
    }

    //Obtener los favoritos actuales
    public LiveData<Favorite[]> getCurrentFavorites() {
        return mDownloadedFavorites;
    }

    /**
     * Obtiene los libros mas nuevos
     */
    public void fetchBooks() {
        Log.d(LOG_TAG, "Recuperar books iniciado");

        //Obtener los datos de la red y pasarselos al LiveData
        AppExecutors.getInstance().networkIO().execute(new BooksLoaderRunnable((List<Book> books) -> {
            mDownloadedBooks.postValue(books.toArray(new Book[0]));
        }));
    }

    /**
     * Obtiene los usuarios mas nuevos
     */
    public void fetchUsers() {
        Log.d(LOG_TAG, "Recuperar usuarios iniciado");

        //Obtener los datos de la red y pasarselos al LiveData
        AppExecutors.getInstance().networkIO().execute(new UsersLoaderRunnable(new OnUsersLoadedListener() {
            @Override
            public void onUsersLoaded(List<User> users) {
                mDownloadedUsers.postValue(users.toArray(new User[0]));
            }
        }));
    }

    /**
     * Obtiene los favoritos mas nuevos
     */
    public void fetchFavorites() {
        Log.d(LOG_TAG, "Recuperar favoritos iniciado");

        //Obtener los datos de la red y pasarselos al LiveData
        AppExecutors.getInstance().networkIO().execute(new FavoritesLoaderRunnable(new OnFavoritesLoadedListener() {
            @Override
            public void onFavoritesLoaded(List<Favorite> favorites) {
                mDownloadedFavorites.postValue(favorites.toArray(new Favorite[0]));
            }
        }));
    }
}
