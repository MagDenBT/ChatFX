package Core;

import UserList.User;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import sun.nio.cs.Surrogate;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.regex.Pattern;

/*
Объект, который отвечает за backup данных
 */
public class DataSaver {

    private final DataSaverListner listner;
    private final String profilFileName = "pr";
    private final String dataPath = "Media";
    private final String profilPath = "Profil";
    private User user;
    private boolean profilRestored = false;

    public DataSaver(DataSaverListner listner) {
        this.listner = listner;
        user = new User();
    }

    public synchronized void restoreProfil() throws IOException, ClassNotFoundException{
        String fullPath = dataPath + "\\"+ profilPath;
        File profilFile = new File(fullPath + "\\" + profilFileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(profilFile));
        user = (User) objectInputStream.readObject();
        objectInputStream.close();
        if (user.getPhotoFileName() != null) {
            Image image = new Image(fullPath + "\\" + user.getPhotoFileName());
            user.setPhoto(image);
        }
        profilRestored = true;
        listner.ProfilUpdated();
    }

    public boolean isProfilRestored() {
        return profilRestored;
    }

    public User getUser() {
        return user;
    }

    public synchronized void saveProfil() throws IOException {
        File path = new File(dataPath + "//" + profilPath);
        boolean created = false;
        if(!path.exists()) {
             created =  path.mkdirs();
        }
        Image prImage = user.getPhoto();
        if (prImage != null) {
            String namePhoto = user.getPhotoFileName();
            String extention = namePhoto.substring(namePhoto.lastIndexOf('.')+1);
            File imageFile = new File(path,namePhoto);
            if(!imageFile.exists()){
                imageFile.createNewFile();

            }
            BufferedImage rIm = SwingFXUtils.fromFXImage(prImage,null );
            ImageIO.write(rIm, extention, imageFile);
        }
        try {
            File profilFile = new File(path, profilFileName);
            if(!profilFile.exists()){
                profilFile.createNewFile();
            }
            if(!profilFile.exists()) profilFile.createNewFile();
            if(profilFile.canWrite()) {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(profilFileName));
                objectOutputStream.writeObject(user);
                objectOutputStream.flush();
                objectOutputStream.close();
                listner.ProfilUpdated();
            }else System.out.println("Не могу записать файл с профилем");
        } catch (IOException e) {
            listner.onException(e.getMessage());
        }



    }



}
