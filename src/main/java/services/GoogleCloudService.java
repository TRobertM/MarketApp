package services;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GoogleCloudService {

    public static void uploadImage(String path, String gameName) throws IOException {
        try {
            Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/googleCloudAuth.json"));
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            String bucketName = "marketapp_images";
            String imageName = gameName + ".png";
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, imageName).setContentType("image/png").build();
            try(InputStream imageFile = new FileInputStream(path)){
                storage.create(blobInfo, imageFile.readAllBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
