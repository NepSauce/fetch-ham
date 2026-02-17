package ham.swing.panels;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

@SuppressWarnings("FieldMayBeFinal")
public class URLBasePanel {
    private JPanel mainPanel;

    public URLBasePanel() {
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Color.WHITE); 
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}