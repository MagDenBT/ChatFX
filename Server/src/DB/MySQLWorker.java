package DB;

import UserList.Sex;
import UserList.User;

import java.sql.*;

public class MySQLWorker implements DBWorker {
    private final String HOST = "localhost";
    private final String PORT = "3306";
    private final String URLAttribute = "?useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true" +
            "&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private final String BASE = "chatBase";
    private final String LOGIN = "root";
    private final String PASSWORD = "624336";
    private Connection connection;


    public MySQLWorker() throws SQLException {
        checkDB();
    }

    private String getPreparedFullURL() {
        String preparedURL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + BASE + URLAttribute;
        return preparedURL;
    }

    private String getPreparedNullBaseURL() {
        String preparedURL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + URLAttribute;
        return preparedURL;
    }

    private void checkDB() throws SQLException {

        Statement statement;
        connection = DriverManager.getConnection(getPreparedNullBaseURL(), LOGIN, PASSWORD);
        statement = connection.createStatement();
        statement.execute("CREATE DATABASE IF NOT EXISTS " + BASE + ";");
        connection = DriverManager.getConnection(getPreparedFullURL(), LOGIN, PASSWORD);
        statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS users(\n" +
                "id INT NOT NULL AUTO_INCREMENT,\n" +
                " login VARCHAR(20) NOT NULL, password VARCHAR(45) NOT NULL, firstName VARCHAR(100) NOT NULL, " +
                "lastName VARCHAR(100) NOT NULL, age int NOT NULL, sex INT NOT NULL, PRIMARY KEY (`id`))\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8\n" +
                "KEY_BLOCK_SIZE = 2;");

    }

    @Override
    public boolean putUser(User user) throws SQLException {
        Statement st = connection.createStatement();
        ResultSet resultSet = st.executeQuery("SELECT login FROM users " +
                "WHERE login = '" + user.getLogin() + "';");

        if(resultSet.next()) return false;

        PreparedStatement ps =
                connection.prepareStatement("INSERT INTO users(login,password,firstName,lastName,age,sex) " +
                        "VALUES(?,?,?,?,?,?)");
        ps.setString(1,user.getLogin());
        ps.setString(2,user.getPassword());
        ps.setString(3,user.getFirstName());
        ps.setString(4, user.getLastName());
        ps.setInt(5, user.getAge());
        ps.setString(6,user.getSex().toString());
        ps.execute();
        return true;
    }

    @Override
    public void updateUser(User user) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE users\n" +
                "SET password = ?,\n" +
                "  firstName = ?,\n" +
                "  lastName = ?,\n" +
                "  age = ?,\n" +
                "  sex = ?\n" +
                "where login = ?");
        ps.setString(1, user.getPassword());
        ps.setString(2, user.getFirstName());
        ps.setString(3, user.getLastName());
        ps.setInt(4, user.getAge());
        ps.setInt(5, user.getSex().getIn());
        ps.setString(6, user.getLogin());
        ps.execute();
    }

    @Override
    public void deleteUser(String login) throws SQLException {
        Statement st = connection.createStatement();
        st.execute("DELETE FROM users WHERE login = '" + login + "';");
    }

    @Override
    public User getUser(String login) throws SQLException {
        Statement st = connection.createStatement();
        String query = "SELECT * FROM users where login = '" + login + "';";
        ResultSet rs = st.executeQuery(query);
        if(rs.next()){
            User user = new User();
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setAge(rs.getInt("age"));
            user.setSex(Sex.findByKey(rs.getInt("sex")));
            user.setLogin(login);
            return user;
        }else
            return null;
    }

    @Override
    public boolean userValidated(String login, String password) throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT login from users " +
                "where login = '" + login + "' and password = '" + password + "';");
        if(rs.next())
            return true;
        else
            return false;
    }
}
