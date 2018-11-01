import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;

public class WebcamViewer extends JFrame implements Runnable, WebcamListener, WindowListener, UncaughtExceptionHandler, ItemListener, WebcamDiscoveryListener {
    private static final long serialVersionUID = 1L;

    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private WebcamPicker picker = null;


    public void run() {
        Webcam.addDiscoveryListener(this);

        setTitle("Java Webcam Capture POC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        addWindowListener(this);

        picker = new WebcamPicker();
        picker.addItemListener(this);

        webcam = picker.getSelectedWebcam();

        if (webcam == null) {
            System.out.println("No webcams found...");
            System.exit(1);
        }
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.addWebcamListener(WebcamViewer.this);

        panel = new WebcamPanel(webcam, false);
        panel.setFPSDisplayed(true);

        add(picker, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        pack();
        setVisible(true);

        Thread t = new Thread() {
            @Override
            public void run() {
                panel.start();
            }
        };

        t.setName("example-starter");
        t.setDaemon(true);
        t.setUncaughtExceptionHandler(this);
        t.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new WebcamViewer());
    }

    public void webcamFound(WebcamDiscoveryEvent webcamDiscoveryEvent) {
        if (picker != null) {
            picker.addItem(webcamDiscoveryEvent.getWebcam());
        }
    }

    public void webcamGone(WebcamDiscoveryEvent webcamDiscoveryEvent) {
        if (picker != null) {
            picker.removeItem(webcamDiscoveryEvent.getWebcam());
        }
    }

    public void webcamOpen(WebcamEvent webcamEvent) {

    }

    public void webcamClosed(WebcamEvent webcamEvent) {

    }

    public void webcamDisposed(WebcamEvent webcamEvent) {

    }

    public void webcamImageObtained(WebcamEvent webcamEvent) {

    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getItem() != webcam) {
            if (webcam != null) {
                panel.stop();
                remove(panel);

                webcam.removeWebcamListener(this);
                webcam.close();

                webcam = (Webcam) e.getItem();
                webcam.setViewSize(WebcamResolution.VGA.getSize());
                webcam.addWebcamListener(this);

                System.out.println("selected " + webcam.getName());

                panel=new WebcamPanel(webcam,false);
                panel.setFPSDisplayed(true);
                add(panel,BorderLayout.CENTER);

                Thread t = new Thread(){
                    @Override
                    public void run(){
                        panel.start();
                    }
                };
                t.setName("example-stoper");
                t.setDaemon(true);
                t.setUncaughtExceptionHandler(this);
                t.start();

            }
        }
    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {

    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {
        System.out.println("webcam viewer paused");
        panel.resume();
    }

    public void windowDeiconified(WindowEvent e) {
        System.out.println("webcam viewer resumed");
        panel.resume();
    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }

    public void uncaughtException(Thread t, Throwable e) {
        System.err.println(String.format("Exeption in thread %s", t.getName()));
        e.printStackTrace();
    }
}
