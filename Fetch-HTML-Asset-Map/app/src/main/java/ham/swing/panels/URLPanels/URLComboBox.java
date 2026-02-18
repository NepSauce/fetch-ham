package ham.swing.panels.URLPanels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;

@SuppressWarnings("FieldMayBeFinal")
public class URLComboBox {  
    private JComboBox<String> modeComboBox;

    public URLComboBox() {
        String[] depthFlag = {"Seed", "Contained"};

        modeComboBox = new JComboBox<>(depthFlag);
        modeComboBox.setSelectedIndex(0);
        modeComboBox.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0)); 
        modeComboBox.setPreferredSize(new java.awt.Dimension(100, 35));
        modeComboBox.setBackground(Color.WHITE);
        modeComboBox.setMaximumSize(new java.awt.Dimension(100, 35));
        modeComboBox.setMinimumSize(new java.awt.Dimension(100, 35));
        
        Font fontA = new Font("Arial", Font.ROMAN_BASELINE, 16);
        modeComboBox.setFont(fontA);
    }

    public JComboBox<String> getModeComboBox() {
        return modeComboBox;
    }
}
