package ham.swing.panels;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import ham.swing.panels.URLPanels.URLMapButton;
import ham.swing.panels.URLPanels.URLStopButton;
import ham.swing.panels.URLPanels.URLSwitchPanel;

@SuppressWarnings("FieldMayBeFinal")
public class URLButtonPanel {
    private JPanel mainControlPanel;
    private URLMapButton urlMapButton;
    private URLSwitchPanel urlSwitchPanel;
    private URLStopButton urlStopButton;

    public URLButtonPanel() {
        mainControlPanel = new JPanel();
        mainControlPanel.setLayout(new BoxLayout(mainControlPanel, BoxLayout.X_AXIS));
        mainControlPanel.setBackground(Color.WHITE);
        mainControlPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        urlMapButton = new URLMapButton();
        urlMapButton.getURLMapButtonPanel().setAlignmentY(Component.CENTER_ALIGNMENT);
        mainControlPanel.add(urlMapButton.getURLMapButtonPanel());

        urlSwitchPanel = new URLSwitchPanel();
        urlSwitchPanel.getSwitchPanel().setAlignmentY(Component.CENTER_ALIGNMENT);
        mainControlPanel.add(urlSwitchPanel.getSwitchPanel());

        mainControlPanel.add(Box.createHorizontalGlue());

        urlStopButton = new URLStopButton();
        urlStopButton.getStopButtonPanel().setAlignmentY(Component.CENTER_ALIGNMENT);
        mainControlPanel.add(urlStopButton.getStopButtonPanel());
    }

    public JPanel getMainControlPanel() {
        return mainControlPanel;
    }

    public URLMapButton getUrlMapButton() {
        return urlMapButton;
    }

    public URLSwitchPanel getUrlSwitchPanel() {
        return urlSwitchPanel;
    }

    public URLStopButton getUrlStopButton() {
        return urlStopButton;
    }
}
