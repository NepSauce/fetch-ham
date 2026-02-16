package ham.swing.frames;

import java.io.File;

import javax.swing.ImageIcon;

public class HAMFrame {
    public HAMFrame() {
        javax.swing.JFrame frame = new javax.swing.JFrame("Fetch-Ham - HTML Asset Map");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);

        File logoFile = new File("media/fetch_ham_logo.png");
        
        if (logoFile.isFile()) {
            frame.setIconImage(new ImageIcon(logoFile.getPath()).getImage());
        }
    }
}
