package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class UsersManager {

    private static ObservableList<WrapperUser> usersList = FXCollections.observableList(new ArrayList<WrapperUser>());

    public static void addUser(WrapperUser wrapperUser) {usersList.add(wrapperUser);}

    public static void removeUser(WrapperUser wrapperUser) {
        usersList.remove(wrapperUser);
    }

    public static ObservableList<WrapperUser> getUsersList(){
        return usersList;
    }
}
