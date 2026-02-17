package ham.swing.panels;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import ham.swing.panels.URLPanels.URLComboBox;

@SuppressWarnings("FieldMayBeFinal")
public class URLBasePanel {
    private JPanel mainPanel;

    public URLBasePanel() {
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Color.WHITE); 

        URLComboBox urlComboBox = new URLComboBox();
        mainPanel.add(urlComboBox.getModeComboBox(), BorderLayout.NORTH);
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}