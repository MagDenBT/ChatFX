package DB;

import UserList.User;

import java.sql.SQLException;

public interface DBWorker {
    /**
     * Возвращает false, если пользователь уже есть в базе
     * иначе добавлет его в базу и возвращает true
     * @param user
     * @return
     * @throws SQLException
     */
    boolean putUser(User user) throws SQLException;

    void updateUser(User user) throws SQLException;

    void deleteUser(String login) throws SQLException;

    User getUser(String login) throws SQLException;

    boolean userValidated(String login, String password) throws SQLException;


}
