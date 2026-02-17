package ham.swing.frames;

import java.awt.Color;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ham.swing.panels.LogoPanel;

public class HAMFrame {
    public HAMFrame() {
        LogoPanel logoPanel = new LogoPanel();
        JFrame hamFrame = new JFrame("Fetch Ham - HTML Asset Map");
        File logoFile = new File("media/fetch_ham_logo.png");

        hamFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hamFrame.setSize(800, 600);
        hamFrame.setLocationRelativeTo(null); 
        hamFrame.setVisible(true);
        hamFrame.setIconImage(new ImageIcon(logoFile.getPath()).getImage());
        hamFrame.add(logoPanel.getLogoHeaderPanel());
        hamFrame.getContentPane().setBackground(new Color(238,238,238,255));

    }
}
