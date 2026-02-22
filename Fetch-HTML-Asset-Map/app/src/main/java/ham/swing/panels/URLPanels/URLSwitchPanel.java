package ham.swing.panels.URLPanels;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

@SuppressWarnings("FieldMayBeFinal")
public class URLSwitchPanel {
    private JToggleButton robotSwitch;
    private JToggleButton rulesSwitch;
    private JPanel switchPanel;
    
    public URLSwitchPanel() {
        switchPanel = new JPanel();
        switchPanel.setLayout(new BoxLayout(switchPanel, BoxLayout.Y_AXIS));
        switchPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        robotSwitch = new JToggleButton("On");
        setComponentSize(robotSwitch, new Dimension(50, 17));

        rulesSwitch = new JToggleButton("On");
        setComponentSize(rulesSwitch, new Dimension(50, 17));

        switchPanel.add(robotSwitch);
        switchPanel.add(rulesSwitch);
    }

    private void setComponentSize(JToggleButton toggleButton, Dimension dimension) {
        toggleButton.setPreferredSize(dimension);
        toggleButton.setMaximumSize(dimension);
        toggleButton.setMinimumSize(dimension);
    }

    public JPanel getSwitchPanel() {
        return switchPanel;
    }
    
}
