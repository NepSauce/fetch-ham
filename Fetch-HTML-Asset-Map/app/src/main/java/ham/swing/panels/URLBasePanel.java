package ham.swing.panels;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import ham.swing.panels.URLPanels.URLComboBox;
import ham.swing.panels.URLPanels.URLPanel;

@SuppressWarnings("FieldMayBeFinal")
public class URLBasePanel {
    private JPanel mainPanel;

    public URLBasePanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setBackground(Color.WHITE); 

        URLComboBox urlComboBox = new URLComboBox();
        mainPanel.add(urlComboBox.getModeComboBox());

        URLPanel urlPanel = new URLPanel();
        mainPanel.add(urlPanel.getURLPanel());
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}