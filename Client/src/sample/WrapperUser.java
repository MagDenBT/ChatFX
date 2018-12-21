package sample;

import UserList.User;
import javafx.beans.property.SimpleStringProperty;

/*
Обертка для класса User, дабы запихнуть поля последнего в TableView
 */

public class WrapperUser {

    private final SimpleStringProperty login;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty firstLastName;


    public WrapperUser(User user) {
        this.login = new SimpleStringProperty(user.getLogin());
        this.firstName = new SimpleStringProperty(user.getFirstName());
        this.lastName = new SimpleStringProperty(user.getLastName());
        firstLastName = new SimpleStringProperty(user.getFirstName() + " " + user.getLastName());
    }

    public String getLogin() {
        return login.get();
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public String getFirstLastName() {
        return firstLastName.get();
    }

    public SimpleStringProperty firstLastNameProperty() {
        return firstLastName;
    }
}
