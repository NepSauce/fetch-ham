package ham.swing.panels;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import ham.swing.panels.URLPanels.URLComboBox;

@SuppressWarnings("FieldMayBeFinal")
public class URLBasePanel {
    private JPanel mainPanel;

    public URLBasePanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setBackground(Color.WHITE); 

        URLComboBox urlComboBox = new URLComboBox();
        mainPanel.add(urlComboBox.getModeComboBox());
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}