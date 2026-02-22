package ham.swing.panels.URLPanels;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

@SuppressWarnings("FieldMayBeFinal")
public class URLRobotPanel {
    private JPanel controlPanel;
    private JButton robotButton;
    private JButton viewRulesButton;
    private JToggleButton robotSwitch;
    private JToggleButton rulesSwitch;

    public URLRobotPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        controlPanel.setPreferredSize(new Dimension(125, 35));
        controlPanel.setMaximumSize(new Dimension(125, 35));
        controlPanel.setMinimumSize(new Dimension(125, 35));
        controlPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        robotButton = new JButton("robots.txt");
        setComponentSize(robotButton, new Dimension(100, 17));
        viewRulesButton = new JButton("View rules");
        setComponentSize(viewRulesButton, new Dimension(100, 17));
        viewRulesButton.setEnabled(false);

        buttonPanel.add(robotButton);
        buttonPanel.add(viewRulesButton);

        JPanel switchPanel = new JPanel();
        switchPanel.setLayout(new BoxLayout(switchPanel, BoxLayout.Y_AXIS));
        switchPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        robotSwitch = new JToggleButton("On");
        setComponentSize(robotSwitch, new Dimension(50, 17));

        rulesSwitch = new JToggleButton("On");
        setComponentSize(rulesSwitch, new Dimension(50, 17));

        switchPanel.add(robotSwitch);
        switchPanel.add(rulesSwitch);

        controlPanel.add(buttonPanel);
        controlPanel.add(switchPanel);
    }

    private void setComponentSize(Component component, Dimension size) {
        component.setPreferredSize(size);
        component.setMaximumSize(size);
        component.setMinimumSize(size);
    }

    public JPanel getControlPanel() {
        return controlPanel;
    }

    public JButton getRobotButton() {
        return robotButton;
    }

    public JButton getViewRulesButton() {
        return viewRulesButton;
    }

    public JToggleButton getRobotSwitch() {
        return robotSwitch;
    }

    public JToggleButton getRulesSwitch() {
        return rulesSwitch;
    }
}
