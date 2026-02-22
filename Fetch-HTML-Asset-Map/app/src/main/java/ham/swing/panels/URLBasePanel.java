package ham.swing.panels;

import java.awt.Color;
import java.awt.Component;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import ham.swing.panels.URLPanels.URLComboBox;
import ham.swing.panels.URLPanels.URLMapButton;
import ham.swing.panels.URLPanels.URLPanel;
import ham.swing.panels.URLPanels.URLSwitchPanel;

@SuppressWarnings("FieldMayBeFinal")
public class URLBasePanel {
    private JPanel mainPanel;

    public URLBasePanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setBackground(Color.WHITE); 
        mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        URLComboBox urlComboBox = new URLComboBox();
        urlComboBox.getModeComboBox().setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.add(urlComboBox.getModeComboBox());

        URLPanel urlPanel = new URLPanel();
        urlPanel.getURLPanel().setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.add(urlPanel.getURLPanel());

        URLMapButton urlMapButton = new URLMapButton();
        urlMapButton.getURLMapButtonPanel().setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.add(urlMapButton.getURLMapButtonPanel());

        URLButtonPanel urlButtonPanel = new URLButtonPanel();
        urlButtonPanel.getMainControlPanel().setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.add(urlButtonPanel.getMainControlPanel());

        URLSwitchPanel urlSwitchPanel = new URLSwitchPanel();
        urlSwitchPanel.getSwitchPanel().setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.add(urlSwitchPanel.getSwitchPanel());
    }

    public JPanel getPanel() {
        return mainPanel;
    }
}