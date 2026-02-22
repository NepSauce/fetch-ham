package ham.swing.panels.URLPanels;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class URLMapButton {
    JPanel mapButtonPanel;

    public URLMapButton() {
        mapButtonPanel = new JPanel();

        mapButtonPanel.setLayout(new BoxLayout(mapButtonPanel, BoxLayout.X_AXIS));
        mapButtonPanel.setBackground(Color.WHITE);
        mapButtonPanel.setPreferredSize(new java.awt.Dimension(100, 35));
        mapButtonPanel.setMaximumSize(new java.awt.Dimension(100, 35));
        mapButtonPanel.setMinimumSize(new java.awt.Dimension(100, 35));
        mapButtonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        JButton mapButton = new JButton("Map");
        mapButton.setPreferredSize(new java.awt.Dimension(100, 35));
        mapButton.setMaximumSize(new java.awt.Dimension(100, 35));
        mapButton.setMinimumSize(new java.awt.Dimension(100, 35));  
        mapButton.setAlignmentY(Component.CENTER_ALIGNMENT);


        mapButtonPanel.add(mapButton);  
    }

    public JPanel getURLMapButtonPanel() {
        return mapButtonPanel;
    }
    
}
