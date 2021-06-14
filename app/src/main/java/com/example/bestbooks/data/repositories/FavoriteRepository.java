package com.example.bestbooks.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.bestbooks.AppExecutors;
import com.example.bestbooks.data.models.Favorite;
import com.example.bestbooks.data.network.ProjectNetworkDataSource;
import com.example.bestbooks.data.roomdb.FavoriteDAO;

import java.util.Arrays;
import java.util.List;

public class FavoriteRepository {
    private static final String LOG_TAG = FavoriteRepository.class.getSimpleName();

    // For Singleton instantiation
    private static FavoriteRepository sInstance;
    private final FavoriteDAO mFavoriteDao;
    private final ProjectNetworkDataSource mProjectNetworkDataSource;
    private final AppExecutors mExecutors = AppExecutors.getInstance();


    private FavoriteRepository(FavoriteDAO favoriteDao, ProjectNetworkDataSource projectNetworkDataSource) {
        mFavoriteDao = favoriteDao;
        mProjectNetworkDataSource = projectNetworkDataSource;

        //LiveData que obtiene favoritos de la red
        LiveData<Favorite[]> networkData = mProjectNetworkDataSource.getCurrentFavorites();

        //Mientras que exista el repositorio, se observa la red con LiveData
        //Si el LiveData cambia se actualiza la base de datos
        networkData.observeForever(newFavoritesFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                //Insertar los nuevos favoritos en la base de datos local
                mFavoriteDao.insertAllFavorites(Arrays.asList(newFavoritesFromNetwork));
                Log.d(LOG_TAG, "Nuevos valores de favoritos insertados en Room");
            });
        });
    }

    public synchronized static FavoriteRepository getInstance(FavoriteDAO favoriteDao, ProjectNetworkDataSource projectNetworkDataSource) {
        Log.d(LOG_TAG, "Obteniendo el repositorio de favoritos");
        if (sInstance == null) {
            sInstance = new FavoriteRepository(favoriteDao, projectNetworkDataSource);
            Log.d(LOG_TAG, "Creado nuevo repositorio de favoritos");
        }
        return sInstance;
    }

    /**
     * Operaciones realacionadas con BD
     **/

    //Obtener favoritos actuales de un usuario
    public LiveData<List<Favorite>> getAllCurrentFavoritesByUser(int userID) {
        return mFavoriteDao.getAllFavoritesByUser(userID);
    }

    //Elimina los favoritos de un book
    public void deleteFavoritesByBook(int bookID) {
        mFavoriteDao.deleteFavoritesByBook(bookID);
    }

    //Comprobar fav de usuario y un book
    public LiveData<Favorite> getFavByUserAndBook(int userID, int bookID) {
        return mFavoriteDao.getFavByUserAndBook(userID, bookID);
    }

    //Insertar favorito
    public void insertFavorite(Favorite favorite) {
        mFavoriteDao.insertFavorite(favorite);
    }

    //Eliminar favorito
    public void deleteFavorite(Favorite favorite) {
        mFavoriteDao.deleteFavorite(favorite);
    }

    //Modifica un favorito
    public void updateFavorite(Favorite favorite) {
        mFavoriteDao.updateFavorite(favorite);
    }
}
