package ham.swing.panels;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import ham.swing.panels.URLPanels.URLControlPanel;

@SuppressWarnings("FieldMayBeFinal")
public class URLButtonPanel {
    private JPanel mainControlPanel;

    public URLButtonPanel() {
        mainControlPanel = new JPanel();
        mainControlPanel.setLayout(new BoxLayout(mainControlPanel, BoxLayout.X_AXIS));
        mainControlPanel.setBackground(Color.WHITE); 
        
        URLControlPanel controlPanel = new URLControlPanel();
        mainControlPanel.add(controlPanel.getControlPanel());

    }

    public JPanel getMainControlPanel() {
        return mainControlPanel;
    }
}
