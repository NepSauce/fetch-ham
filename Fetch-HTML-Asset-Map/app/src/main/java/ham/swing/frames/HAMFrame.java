package ham.swing.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ham.swing.panels.LogoPanel;

public class HAMFrame extends JFrame{
    public HAMFrame() {
        File logoFile = new File("media/fetch_ham_logo.png");

        setTitle("Fetch Ham - HTML Asset Map");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());
        setVisible(true);
        setIconImage(new ImageIcon(logoFile.getPath()).getImage());
        getContentPane().setBackground(new Color(238,238,238,255));

        LogoPanel logoPanel = new LogoPanel();
        JPanel logoWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoWrapper.add(logoPanel.getLogoHeaderPanel());

        // Add logoWrapper instead of logoPanel directly
        add(logoWrapper, BorderLayout.PAGE_START);
    }
}
