package ham.swing.panels.URLPanels;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("FieldMayBeFinal")
public class URLMapButton {
    private JPanel mapButtonPanel;
    private JButton mapButton;

    public URLMapButton() {
        mapButtonPanel = new JPanel();

        mapButtonPanel.setLayout(new BoxLayout(mapButtonPanel, BoxLayout.X_AXIS));
        mapButtonPanel.setBackground(Color.WHITE);
        mapButtonPanel.setPreferredSize(new java.awt.Dimension(100, 34));
        mapButtonPanel.setMaximumSize(new java.awt.Dimension(100, 34));
        mapButtonPanel.setMinimumSize(new java.awt.Dimension(100, 34));
        mapButtonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        mapButton = new JButton("Map URL");
        mapButton.setPreferredSize(new java.awt.Dimension(100, 34));
        mapButton.setMaximumSize(new java.awt.Dimension(100, 34));
        mapButton.setMinimumSize(new java.awt.Dimension(100, 34));
        mapButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        mapButtonPanel.add(mapButton);
    }

    public JPanel getURLMapButtonPanel() {
        return mapButtonPanel;
    }

    public JButton getMapButton() {
        return mapButton;
    }
}
