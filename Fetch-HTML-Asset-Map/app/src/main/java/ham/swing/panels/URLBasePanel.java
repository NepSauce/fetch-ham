package ham.swing.panels;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import ham.swing.panels.URLPanels.URLComboBox;
import ham.swing.panels.URLPanels.URLPanel;
import ham.swing.panels.URLPanels.URLSwitchPanel;

@SuppressWarnings("FieldMayBeFinal")
public class URLBasePanel {
    private JPanel mainPanel;
    private URLComboBox urlComboBox;
    private URLPanel urlPanel;
    private URLButtonPanel urlButtonPanel;

    public URLBasePanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setBackground(Color.WHITE); 
        mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        urlComboBox = new URLComboBox();
        urlComboBox.getModeComboBox().setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.add(urlComboBox.getModeComboBox());

        urlPanel = new URLPanel();
        urlPanel.getURLPanel().setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.add(urlPanel.getURLPanel());

        urlButtonPanel = new URLButtonPanel();
        urlButtonPanel.getMainControlPanel().setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.add(urlButtonPanel.getMainControlPanel());
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JComboBox<String> getModeComboBox() {
        return urlComboBox.getModeComboBox();
    }

    public JTextField getUrlField() {
        return urlPanel.getURLPanel();
    }

    public URLButtonPanel getUrlButtonPanel() {
        return urlButtonPanel;
    }

    public URLSwitchPanel getUrlSwitchPanel() {
        return urlButtonPanel.getUrlSwitchPanel();
    }
}