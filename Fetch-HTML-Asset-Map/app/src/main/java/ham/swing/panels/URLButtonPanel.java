package ham.swing.panels;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import ham.swing.panels.URLPanels.URLMapButton;
import ham.swing.panels.URLPanels.URLSwitchPanel;

@SuppressWarnings("FieldMayBeFinal")
public class URLButtonPanel {
    private JPanel mainControlPanel;

    public URLButtonPanel() {
        mainControlPanel = new JPanel();
        mainControlPanel.setLayout(new BoxLayout(mainControlPanel, BoxLayout.X_AXIS));
        mainControlPanel.setBackground(Color.WHITE);
        mainControlPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        URLMapButton urlMapButton = new URLMapButton();
        urlMapButton.getURLMapButtonPanel().setAlignmentY(Component.CENTER_ALIGNMENT);
        mainControlPanel.add(urlMapButton.getURLMapButtonPanel());

        URLSwitchPanel urlSwitchPanel = new URLSwitchPanel();
        urlSwitchPanel.getSwitchPanel().setAlignmentY(Component.CENTER_ALIGNMENT);
        mainControlPanel.add(urlSwitchPanel.getSwitchPanel());
    }

    public JPanel getMainControlPanel() {
        return mainControlPanel;
    }
}
