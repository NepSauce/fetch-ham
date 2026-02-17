package ham.swing.panels;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

@SuppressWarnings("FieldMayBeFinal")
public class LogoPanel {
    private JPanel headerPanel;
    private File logoFile;
    private ImageIcon logoIcon;
    private Image scaledImage;
    private JLabel logoLabel;
    private JLabel nameLabel;

    public LogoPanel() {
        headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        logoFile = new File("media/fetch_ham_logo.png");
        logoIcon = new ImageIcon(logoFile.getPath());
        scaledImage = logoIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(scaledImage));
        nameLabel = new JLabel("Fetch Ham");

        nameLabel.setFont(nameLabel.getFont().deriveFont(18f));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        headerPanel.add(logoLabel);
        headerPanel.add(nameLabel);
    }

    public JPanel getLogoHeaderPanel() {
        return headerPanel;
    }
}
