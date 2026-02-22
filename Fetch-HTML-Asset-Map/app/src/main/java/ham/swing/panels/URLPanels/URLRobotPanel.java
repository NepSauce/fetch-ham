package ham.swing.panels.URLPanels;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

@SuppressWarnings("FieldMayBeFinal")
public class URLRobotPanel {
    private JPanel controlPanel;

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

        JButton robotButton = new JButton("robots.txt");
        robotButton.setPreferredSize(new Dimension(100, 17));
        robotButton.setMaximumSize(new Dimension(100, 17));
        robotButton.setMinimumSize(new Dimension(100, 17));
        JButton viewRulesButton = new JButton("View rules");
        viewRulesButton.setPreferredSize(new Dimension(100, 17));
        viewRulesButton.setMaximumSize(new Dimension(100, 17));
        viewRulesButton.setMinimumSize(new Dimension(100, 17));
        viewRulesButton.setEnabled(false);

        buttonPanel.add(robotButton);
        buttonPanel.add(viewRulesButton);

        JPanel switchPanel = new JPanel();
        switchPanel.setLayout(new BoxLayout(switchPanel, BoxLayout.Y_AXIS));
        switchPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JToggleButton robotSwitch = new JToggleButton("On");
        robotSwitch.setPreferredSize(new Dimension(50, 17));
        robotSwitch.setMaximumSize(new Dimension(50, 17));
        robotSwitch.setMinimumSize(new Dimension(50, 17));

        JToggleButton rulesSwitch = new JToggleButton("On");
        rulesSwitch.setPreferredSize(new Dimension(50, 17));
        rulesSwitch.setMaximumSize(new Dimension(50, 17));
        rulesSwitch.setMinimumSize(new Dimension(50, 17));

        switchPanel.add(robotSwitch);
        switchPanel.add(rulesSwitch);

        controlPanel.add(buttonPanel);
        controlPanel.add(Box.createHorizontalStrut(0));
        controlPanel.add(switchPanel);
    }

    public JPanel getURLRobotPanel() {
        return controlPanel;
    }
}
