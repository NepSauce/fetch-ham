package ham.swing.panels;

import java.awt.Color;
import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
        headerPanel.setLayout(new javax.swing.BoxLayout(headerPanel, javax.swing.BoxLayout.X_AXIS));
        logoFile = new File("app/src/main/resources/media/fetch_ham_logo.png");
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
        headerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        headerPanel.setPreferredSize(new java.awt.Dimension(165, 45));

        headerPanel.add(logoLabel);
        headerPanel.add(Box.createHorizontalStrut(5));
        headerPanel.add(nameLabel1);
        headerPanel.add(Box.createHorizontalStrut(5));
        headerPanel.add(nameLabel2);
        headerPanel.add(Box.createHorizontalStrut(5));
    }

    public JPanel getLogoHeaderPanel() {
        return headerPanel;
    }
}
