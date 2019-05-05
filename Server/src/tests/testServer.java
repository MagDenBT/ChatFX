package tests;

import DB.DBWorker;
import UserList.Sex;
import UserList.User;
import DB.MySQLWorker;

import java.sql.SQLException;

public class testServer {
    public static void main(String[] args) throws SQLException {
        DBWorker db = new MySQLWorker();
        //
        User user = new User();
        user.setLogin("areal");
        user.setPassword("myPass");
        user.setSex(Sex.MAN);
        user.setAge(24);
        user.setFirstName("Deni");
        user.setLastName("Bilto");
        //
        User secondUser;
        boolean exist;
        boolean done;

        secondUser = db.getUser(user.getLogin());
        exist = db.userValidated(user.getLogin(), user.getPassword());
        done = db.putUser(user);
        user.setLastName("NONAME");
        db.updateUser(user);

        secondUser = db.getUser(user.getLogin());
        db.deleteUser(user.getLogin());
        secondUser = db.getUser(user.getLogin());

    }
}
