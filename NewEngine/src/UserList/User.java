package UserList;

import javafx.scene.image.Image;

import java.io.Serializable;


/*
Этот объект используется, как универсальное решение, для хранении и передачи информации о пользователе через сокет.
В каждом сообщении(объекте класса Message) содержится переменная объекта User, т.к. другого способа проводить авторизацию
и обмениваться информацией о пользователях я не придумал.Таки дела
 */

public class User implements Serializable {

    private  String login;
    private  String password;
    private  String firstName;
    private  String lastName;
    private  int age;
    private  String sex;
    private  transient Image photo;
    private String PhotoFileName;

    public User(){}

    public User(String login, String password, String firstName, String lastName, int age, String sex, Image photo) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.sex = sex;
        this.photo = photo;
    }

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

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public String getPhotoFileName() {
        return PhotoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        PhotoFileName = photoFileName;
    }
}
