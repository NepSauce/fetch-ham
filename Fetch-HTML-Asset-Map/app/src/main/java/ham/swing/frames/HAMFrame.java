package ham.swing.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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

        JMenuBar menuBar = new JMenuBar();
        // menuBar.add(Box.createRigidArea(new Dimension(165, 0)));

        // Create menus
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");

        // Add menu items to File menu
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Add menu items to Edit menu
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        // Add menu items to Help menu
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        // Set the menu bar for the frame
        setJMenuBar(menuBar);

        LogoPanel logoPanel = new LogoPanel();
        JPanel logoWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoWrapper.add(logoPanel.getLogoHeaderPanel());

        // Add logoWrapper instead of logoPanel directly
        add(logoWrapper, BorderLayout.PAGE_START);
    }
}
