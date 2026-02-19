package ham.swing.panels.URLPanels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;

@SuppressWarnings("FieldMayBeFinal")
public class URLPanel {
    private JTextField urlTextArea;

    public URLPanel() {
        urlTextArea = new JTextField();
        urlTextArea.setPreferredSize(new java.awt.Dimension(350, 35));
        urlTextArea.setMaximumSize(new java.awt.Dimension(350, 35));
        urlTextArea.setMinimumSize(new java.awt.Dimension(350, 35));
        urlTextArea.setBackground(new Color(238,238,238,255));
        urlTextArea.setForeground(new Color(39,39,37, 255));
        urlTextArea.setSize(new java.awt.Dimension(350, 35));
        urlTextArea.setEditable(true);
        urlTextArea.setText("https://");
        urlTextArea.setMargin(new java.awt.Insets(0, 5, 0, 0));

        Font fontA = new Font("Arial", Font.PLAIN, 20);
        urlTextArea.setFont(fontA); 
    }

    public JTextField getURLPanel() {
        return urlTextArea;
    }
}
    