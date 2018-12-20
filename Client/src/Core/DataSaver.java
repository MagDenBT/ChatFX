package Core;

import New.UserList.User;

import java.io.*;
/*
Объект, который отвечает за backup таких данных, как логин/пароль, история переписок и т.п.
 */
public class DataSaver {
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private final String settingsFileName;
    private final String msgLogFileName;
    private final String profilFileName;

    public DataSaver(String settingsFileName, String msgLogFileName, String profilFileName) {
        this.settingsFileName = settingsFileName;
        this.msgLogFileName = msgLogFileName;
        this.profilFileName = profilFileName;
    }

    public boolean saveProfil(User user) {

        File file = new File(profilFileName);
        try {
            file.createNewFile();
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(user);
            objectOutputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


    public User restoreProfilFromFile() {

        File file = new File(profilFileName);

        try {
            file.createNewFile();
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
            return (User) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
