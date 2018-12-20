package Core;

import New.UserList.User;

import java.io.*;
/*
Объект, который отвечает за backup таких данных, как логин/пароль, история переписок и т.п.
 */
public class DataSaver {
    private User user;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private DataSaverListener dataSaverListener;



    public void saveProfil(String fileName, User user) {

        File file = new File("savTest");

        try {
            file.createNewFile();
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(user);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String restoreProfilFromFile(String fileName) {

        File file = new File("savTest");

        try {
            file.createNewFile();
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
            return ((User)objectInputStream.readObject()).getLogin();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
