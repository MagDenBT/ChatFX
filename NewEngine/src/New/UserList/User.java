package New.UserList;

import javafx.scene.image.Image;

import java.io.Serializable;

public class User implements Serializable {

    private final String login;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final int age;
    private final String sex;
    private final Image photo;

    public User(String login, String password, String firstName, String lastName, int age, String sex) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.sex = sex;
        this.photo = null;
    }

    public User(String login, String password, String firstName, String lastName, int age) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.sex = null;
        this.photo = null;
    }
    public User(String login, String password, String firstName, String lastName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = 0;
        this.sex = null;
        this.photo = null;
    }

    public User(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = null;
        this.age = 0;
        this.sex = null;
        this.photo = null;

    }
    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.firstName = null;
        this.lastName = null;
        this.age = 0;
        this.sex = null;
        this.photo = null;
    }

    public User(String login) {
        this.login = login;
        this.password = null;
        this.firstName = null;
        this.lastName = null;
        this.age = 0;
        this.sex = null;
        this.photo = null;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public Image getPhoto() {
        return photo;
    }
}
