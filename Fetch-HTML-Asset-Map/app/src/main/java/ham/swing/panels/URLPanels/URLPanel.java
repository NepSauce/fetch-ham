package ham.swing.panels.URLPanels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

@SuppressWarnings("FieldMayBeFinal")
public class URLPanel {
    private JTextArea urlTextArea;

    public URLPanel() {
        urlTextArea = new JTextArea();
        urlTextArea.setLineWrap(true);
        urlTextArea.setWrapStyleWord(true);
        urlTextArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
        urlTextArea.setPreferredSize(new java.awt.Dimension(200, 35));
        urlTextArea.setMaximumSize(new java.awt.Dimension(200, 35));
        urlTextArea.setMinimumSize(new java.awt.Dimension(200, 35));
        urlTextArea.setBackground(new Color(238,238,238,255));
        urlTextArea.setSize(new java.awt.Dimension(200, 35));

        Font fontA = new Font("Arial", Font.PLAIN, 20);
        urlTextArea.setFont(fontA); 
    }

    public JTextArea getURLPanel() {
        return urlTextArea;
    }
}
    