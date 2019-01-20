package UserList;

import javafx.scene.image.Image;

import java.io.Serializable;


/*
Этот объект используется, как универсальное решение, для хранения и передачи информации о пользователе через сокет.
В каждом сообщении(объекте класса Message) содержится переменная объекта User, т.к. другого способа проводить авторизацию
и обмениваться информацией о пользователях я не придумал.Таки дела
 */

public class User implements Serializable {

    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private int age;
    private String sex;
    private transient Image photo;
    private String photoExtention;

    public User() {
    }

    public User(String login) {
        this.login = login;
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

    public String getPhotoExtention() {
        return photoExtention;
    }

    public void setPhotoExtention(String extention) {
        photoExtention = extention;
    }
}
