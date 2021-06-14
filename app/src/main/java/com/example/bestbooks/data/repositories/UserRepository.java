package com.example.bestbooks.data.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.bestbooks.AppExecutors;
import com.example.bestbooks.data.models.User;
import com.example.bestbooks.data.network.ProjectNetworkDataSource;
import com.example.bestbooks.data.roomdb.UserDAO;

import java.util.Arrays;
import java.util.List;

public class UserRepository {
    private static final String LOG_TAG = UserRepository.class.getSimpleName();

    // For Singleton instantiation
    private static UserRepository sInstance;
    private final UserDAO mUserDao;
    private final ProjectNetworkDataSource mProjectNetworkDataSource;
    private final AppExecutors mExecutors = AppExecutors.getInstance();


    private UserRepository(UserDAO userDao, ProjectNetworkDataSource projectNetworkDataSource) {
        mUserDao = userDao;
        mProjectNetworkDataSource = projectNetworkDataSource;

        //LiveData que obtiene usuarios de la red
        LiveData<User[]> networkData = mProjectNetworkDataSource.getCurrentUsers();

        //Mientras que exista el repositorio, se observa la red con LiveData
        //Si el LiveData cambia se actualiza la base de datos
        networkData.observeForever(newUsersFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                //Insertar los nuevos usuarios en la base de datos local
                mUserDao.insertAllUsers(Arrays.asList(newUsersFromNetwork));
                Log.d(LOG_TAG, "Nuevos valores de usuarios insertados en Room");
            });
        });
    }

    public synchronized static UserRepository getInstance(UserDAO userDao, ProjectNetworkDataSource projectNetworkDataSource) {
        Log.d(LOG_TAG, "Obteniendo el repositorio de usuarios");
        if (sInstance == null) {
            sInstance = new UserRepository(userDao, projectNetworkDataSource);
            Log.d(LOG_TAG, "Creado nuevo repositorio de books");
        }
        return sInstance;
    }

    /**
     * Operaciones realacionadas con BD
     **/

    //Obtener usuarios actuales
    public LiveData<List<User>> getAllCurrentUsers() {
        return mUserDao.getAllUsers();
    }


    //Obtener usuario por su email
    public LiveData<List<User>> getUserByEmail(String emailUser) {
        return mUserDao.getUserByEmail(emailUser);
    }

    //Obtener usuario por su ID
    public LiveData<User> getUserByID(int userID) {
        return mUserDao.getUserByID(userID);
    }

    //Inserta un usuario
    public long insertUser(User user){
          return  mUserDao.insertUser(user);
    }

    //Actualizar un usuario
    public void updateUser(User user){
        AppExecutors.getInstance().diskIO().execute(() -> mUserDao.updateUser(user));
    }

}
