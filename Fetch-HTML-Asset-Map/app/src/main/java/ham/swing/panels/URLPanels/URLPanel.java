package ham.swing.panels.URLPanels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

@SuppressWarnings("FieldMayBeFinal")
public class URLPanel {
    private JTextField urlTextArea;

    public URLPanel() {
        urlTextArea = new JTextField();
        urlTextArea.setPreferredSize(new java.awt.Dimension(350, 35));
        urlTextArea.setMaximumSize(new java.awt.Dimension(350, 35));
        urlTextArea.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        urlTextArea.setMinimumSize(new java.awt.Dimension(350, 35));
        urlTextArea.setBackground(new Color(238,238,238,255));
        urlTextArea.setSize(new java.awt.Dimension(350, 35));
        urlTextArea.setEditable(true);
        urlTextArea.setMargin(new java.awt.Insets(0, 50, 0, 0));

        Font fontA = new Font("Arial", Font.PLAIN, 20);
        urlTextArea.setFont(fontA); 
    }

    public JTextField getURLPanel() {
        return urlTextArea;
    }
}
    