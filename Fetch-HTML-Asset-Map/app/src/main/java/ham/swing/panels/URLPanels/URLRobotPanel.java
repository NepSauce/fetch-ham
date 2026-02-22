package ham.swing.panels.URLPanels;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class URLRobotPanel {
    private JPanel buttonPanel;

    public URLRobotPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));

        JButton robotButton = new JButton("robots.txt");
        JButton viewRulesButton = new JButton("View rules");

        buttonPanel.add(robotButton);
        buttonPanel.add(viewRulesButton);
    }

    public JPanel getControlPanel() {
        return buttonPanel;
    }
}
