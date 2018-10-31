import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamResolution;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class WebCamTutorial {
    public static void main(String[] args) throws IOException {
        Webcam webcam = Webcam.getDefault();

        webcam.addWebcamListener(new WebcamListener() {
            public void webcamOpen(WebcamEvent webcamEvent) {
                System.out.println("Webcam open");
            }

            public void webcamClosed(WebcamEvent webcamEvent) {
                System.out.println("Webcam close");
            }

            public void webcamDisposed(WebcamEvent webcamEvent) {
                System.out.println("Webcam disposed");
            }

            public void webcamImageObtained(WebcamEvent webcamEvent) {
                System.out.println("Image Taken");
            }
        });



        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.open();
        ImageIO.write(webcam.getImage(),"PNG", new File("/home/coder/Desktop/image.png"));
        webcam.close();
    }
}
