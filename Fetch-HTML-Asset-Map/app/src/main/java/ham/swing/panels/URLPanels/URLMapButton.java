package ham.swing.panels.URLPanels;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

public class URLMapButton {
    JPanel mapButtonPanel;

    public URLMapButton() {
        mapButtonPanel = new JPanel();

        mapButtonPanel.setBackground(new java.awt.Color(Color.WHITE.getRGB()));
        mapButtonPanel.setPreferredSize(new java.awt.Dimension(100, 35));
        mapButtonPanel.setMaximumSize(new java.awt.Dimension(100, 35));
        mapButtonPanel.setMinimumSize(new java.awt.Dimension(100, 35));
        
        JButton mapButton = new JButton("Map");
        mapButton.setPreferredSize(new java.awt.Dimension(100, 17));
        mapButton.setMaximumSize(new java.awt.Dimension(100, 17));
        mapButton.setMinimumSize(new java.awt.Dimension(100, 17));  

        mapButtonPanel.add(mapButton);  
    }

    public JPanel getURLMapButtonPanel() {
        return mapButtonPanel;
    }
    
}
