package ham.swing.panels.URLPanels;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;

public class URLComboBox {
    private JComboBox<String> modeComboBox;

    public URLComboBox() {
        String[] depthFlag = {"Seed", "Contained"};

        modeComboBox = new JComboBox<>(depthFlag);
        modeComboBox.setSelectedIndex(0);
        modeComboBox.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0)); 
        modeComboBox.setPreferredSize(new java.awt.Dimension(90, 35));
        modeComboBox.setBackground(Color.WHITE);
        modeComboBox.setMaximumSize(new java.awt.Dimension(90, 35));
        modeComboBox.setMinimumSize(new java.awt.Dimension(90, 35));
    }

    public JComboBox<String> getModeComboBox() {
        return modeComboBox;
    }
}
