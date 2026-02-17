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
    private JLabel nameLabel;

    public LogoPanel() {
        headerPanel = new JPanel();
        logoFile = new File("media/fetch_ham_logo.png");
        logoIcon = new ImageIcon(logoFile.getPath());
        scaledImage = logoIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(scaledImage));
        nameLabel = new JLabel("Fetch Ham");

        nameLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBounds(5, 5, 150, 40);
        headerPanel.setLayout(null);

        logoLabel.setBounds(8, 5, 32, 32);
        nameLabel.setBounds(48, 5, 100, 32);

        headerPanel.add(logoLabel);
        headerPanel.add(nameLabel);

        headerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
    }

    public JPanel getLogoHeaderPanel() {
        return headerPanel;
    }
}
