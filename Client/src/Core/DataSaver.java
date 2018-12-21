package Core;

import UserList.User;

import java.io.*;
/*
Объект, который отвечает за backup таких данных, как логин/пароль, история переписок и т.п.
 */
public class DataSaver {
    private static String settingsFileName = "settings";
    private static String msgLogFileName = "msgLog";
    private static String profilFileName = "pr";
    private User user;

    public DataSaver() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(profilFileName));
        user = (User) objectInputStream.readObject();
        objectInputStream.close();
    }


    public User getUser() {
        return user;
    }

    public static boolean createProfil(User user){

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(profilFileName));
            objectOutputStream.writeObject(user);
            objectOutputStream.flush();
            objectOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

}
