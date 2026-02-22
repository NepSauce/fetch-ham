package ham.swing.panels;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import ham.swing.panels.URLPanels.URLRobotPanel;

@SuppressWarnings("FieldMayBeFinal")
public class URLButtonPanel {
    private JPanel mainControlPanel;

    public URLButtonPanel() {
        mainControlPanel = new JPanel();
        mainControlPanel.setLayout(new BoxLayout(mainControlPanel, BoxLayout.X_AXIS));
        mainControlPanel.setBackground(Color.WHITE); 
        
        URLRobotPanel controlPanel = new URLRobotPanel();
        mainControlPanel.add(controlPanel.getControlPanel());

    }

    public JPanel getMainControlPanel() {
        return mainControlPanel;
    }
}
