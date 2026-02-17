package ham.swing.panels;

import java.awt.Color;
import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

@SuppressWarnings("FieldMayBeFinal")
public class LogoPanel{
    private JPanel headerPanel;
    private File logoFile;
    private ImageIcon logoIcon;
    private Image scaledImage;
    private JLabel logoLabel;
    private JLabel nameLabel1;
    private JLabel nameLabel2;

    public LogoPanel() {
        headerPanel = new JPanel();
        logoFile = new File("media/fetch_ham_logo.png");
        logoIcon = new ImageIcon(logoFile.getPath());
        scaledImage = logoIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(scaledImage));
        nameLabel1 = new JLabel("Fetch");
        nameLabel2 = new JLabel("Ham");

        nameLabel1.setFont(new java.awt.Font("Montserrat", java.awt.Font.BOLD, 20));
        nameLabel1.setForeground(new Color(39,39,37, 255));
        nameLabel2.setFont(new java.awt.Font("Montserrat", java.awt.Font.BOLD, 20));
        nameLabel2.setForeground(new Color(158,54,84, 255));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBounds(5, 5, 150, 40);
        headerPanel.setLayout(null);

        logoLabel.setBounds(8, 5, 32, 32);
        nameLabel1.setBounds(45, 5, 100, 32);
        nameLabel2.setBounds(100, 5, 100, 32);

        headerPanel.add(logoLabel);
        headerPanel.add(nameLabel1);
        headerPanel.add(nameLabel2);

        headerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
    }

    public JPanel getLogoHeaderPanel() {
        return headerPanel;
    }
}
