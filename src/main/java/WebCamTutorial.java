import com.github.sarxos.webcam.Webcam;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class WebCamTutorial {
    public static void main(String[] args) throws IOException {
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        ImageIO.write(webcam.getImage(),"JPG", new File("/home/coder/Desktop/image.jpg"));
    }
}
