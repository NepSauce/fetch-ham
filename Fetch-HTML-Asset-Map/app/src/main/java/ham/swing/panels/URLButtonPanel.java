package ham.swing.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

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
        mainControlPanel.setPreferredSize(new Dimension(125, 35));
        mainControlPanel.setMaximumSize(new Dimension(125, 35));
        mainControlPanel.setMinimumSize(new Dimension(125, 35));
        mainControlPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        URLRobotPanel controlPanel = new URLRobotPanel();
        mainControlPanel.add(controlPanel.getControlPanel());

    }

    public JPanel getMainControlPanel() {
        return mainControlPanel;
    }
}
