package ham.swing.panels.URLPanels;

import javax.swing.JComboBox;

public class URLComboBox {
    private JComboBox<String> modeComboBox;

    public URLComboBox() {
        String[] depthFlag = {"Seed", "Contained"};

        modeComboBox = new JComboBox<>(depthFlag);
        modeComboBox.setSelectedIndex(0); // Default to "Seed"
        modeComboBox.setPreferredSize(new java.awt.Dimension(50, 45));
        modeComboBox.setMaximumSize(new java.awt.Dimension(50, 45));
        modeComboBox.setMinimumSize(new java.awt.Dimension(50, 45));
    }

    public JComboBox<String> getModeComboBox() {
        return modeComboBox;
    }
}
