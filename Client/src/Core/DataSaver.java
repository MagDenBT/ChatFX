package Core;

import UserList.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/*
Объект, который отвечает за backup/restore данных
 */
public class DataSaver {

    private final DataSaverListner listner;
    private final String dataPath = "Media";
    private final String profilePath = "Profil";
    private final String profileFileName = "pr";
    private final String avatarName = "avatar.";
    private User user;

    public DataSaver(DataSaverListner listner) {
        this.listner = listner;
        user = new User();
    }

    public synchronized void restoreProfile() throws IOException, ClassNotFoundException {
        String fullPath = dataPath + "\\" + profilePath;

        File profileFile = new File(fullPath + "\\" + profileFileName);
        FileInputStream inputStream = new FileInputStream(profileFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        user = (User) objectInputStream.readObject();
        objectInputStream.close();
        Image image = null;
        if (user.getPhotoExtention() != null) {
            File photo = new File(fullPath, avatarName + user.getPhotoExtention());
            if (photo.exists()) {
                FileInputStream fis = new FileInputStream(photo);
                image = new Image(fis);
            } else
                image = new Image("Assets/emptyPhoto.png");
        } else
            image = new Image("Assets/emptyPhoto.png");

        user.setPhoto(image);
        listner.ProfilUpdated();
    }

    public User getUser() {
        return user;
    }

    public synchronized void saveProfile() throws IOException {
        File path = new File(dataPath + "//" + profilePath);
        if (!path.exists()) path.mkdirs();
        String extention = user.getPhotoExtention();
        if (extention != null) {
            Image prImage = user.getPhoto();
            File imageFile = new File(path, avatarName + extention);
            if (!imageFile.exists())
                imageFile.createNewFile();
            BufferedImage rIm = SwingFXUtils.fromFXImage(prImage, null);
            ImageIO.write(rIm, extention, imageFile);
        }
        try {
            File profilFile = new File(path, profileFileName);
            if (!profilFile.exists()) {
                profilFile.createNewFile();
            }
            if (profilFile.canWrite()) {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(profilFile));
                objectOutputStream.writeObject(user);
                objectOutputStream.flush();
                objectOutputStream.close();
                listner.ProfilUpdated();
            } else System.out.println("Не могу записать файл с профилем");
        } catch (IOException e) {
            listner.onException(e.getMessage());
        }
    }


}
