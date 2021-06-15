package com.example.bestbooks;

import android.content.Context;

import com.example.bestbooks.addBook.AddBookVMFactory;
import com.example.bestbooks.data.network.ProjectNetworkDataSource;
import com.example.bestbooks.data.repositories.BookRepository;
import com.example.bestbooks.data.repositories.FavoriteRepository;
import com.example.bestbooks.data.repositories.UserRepository;
import com.example.bestbooks.data.roomdb.ProjectDatabase;
import com.example.bestbooks.detailBook.DetailBookVMFactory;
import com.example.bestbooks.login.LoginVMFactory;
import com.example.bestbooks.modifyBook.ModifyBookVMFactory;
import com.example.bestbooks.modifyProfile.ModifyProfileVMFactory;
import com.example.bestbooks.principal.PrincipalVMFactory;
import com.example.bestbooks.profile.ProfileVMFactory;
import com.example.bestbooks.register.RegisterVMFactory;
import com.example.bestbooks.search.SearchVMFactory;
import com.example.bestbooks.userBooks.UserBooksVMFactory;
import com.example.bestbooks.userFavs.UserFavsVMFactory;

public class AppContainer {

    //BASE DE DATOS LOCAL Y REMOTA
    private ProjectDatabase databaseRoom;
    private ProjectNetworkDataSource networkDataSource;


    //REPOSITORIES
    public BookRepository bookRepository;
    public UserRepository userRepository;
    public FavoriteRepository favoriteRepository;


    //FACTORIES
    public PrincipalVMFactory principalVMFactory;
    public AddBookVMFactory addBookVMFactory;
    public LoginVMFactory loginVMFactory;
    public DetailBookVMFactory  detailBookVMFactory;
    public ModifyBookVMFactory modifyBookVMFactory;
    public ProfileVMFactory profileVMFactory;
    public ModifyProfileVMFactory modifyProfileVMFactory;
    public UserBooksVMFactory userBooksVMFactory;
    public RegisterVMFactory registerVMFactory;
    public UserFavsVMFactory userFavsVMFactory;
    public SearchVMFactory searchVMFactory;


    public AppContainer(Context context){

        databaseRoom = ProjectDatabase.getInstance(context);
        networkDataSource = ProjectNetworkDataSource.getInstance();


        //REPOSITORIES
        bookRepository = BookRepository.getInstance(databaseRoom.getBookDAO(), networkDataSource);
        userRepository = UserRepository.getInstance(databaseRoom.getUserDAO(), networkDataSource);
        favoriteRepository = FavoriteRepository.getInstance(databaseRoom.getFavoriteDAO(), networkDataSource);


        //FACTORIES
        principalVMFactory = new PrincipalVMFactory(bookRepository);
        addBookVMFactory = new AddBookVMFactory(bookRepository);
        loginVMFactory = new LoginVMFactory(userRepository);
        detailBookVMFactory = new DetailBookVMFactory(userRepository, bookRepository, favoriteRepository);
        modifyBookVMFactory = new ModifyBookVMFactory(bookRepository);
        profileVMFactory = new ProfileVMFactory(userRepository, bookRepository, favoriteRepository);
        modifyProfileVMFactory = new ModifyProfileVMFactory(userRepository);
        userBooksVMFactory = new UserBooksVMFactory(bookRepository);
        registerVMFactory = new RegisterVMFactory(userRepository);
        userFavsVMFactory = new UserFavsVMFactory(bookRepository, favoriteRepository);
        searchVMFactory = new SearchVMFactory(bookRepository);
    }
}
