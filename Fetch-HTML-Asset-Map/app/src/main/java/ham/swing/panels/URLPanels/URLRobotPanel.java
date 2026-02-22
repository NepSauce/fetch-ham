package ham.swing.panels.URLPanels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class URLRobotPanel {
    private JPanel buttonPanel;

    public URLRobotPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton robotButton = new JButton("robots.txt");
        robotButton.setPreferredSize(new Dimension(70, 17));
        robotButton.setMaximumSize(new Dimension(70, 17));
        robotButton.setMinimumSize(new Dimension(70, 17));
        robotButton.setBorder(BorderFactory.createLineBorder(new Color(124, 124, 124, 255), 1));
        JButton viewRulesButton = new JButton("View rules");
        viewRulesButton.setPreferredSize(new Dimension(70, 17));
        viewRulesButton.setMaximumSize(new Dimension(70, 17));
        viewRulesButton.setMinimumSize(new Dimension(70, 17));
        viewRulesButton.setBorder(BorderFactory.createLineBorder(new Color(124, 124, 124, 255), 1));

        buttonPanel.add(robotButton);
        buttonPanel.add(viewRulesButton);
    }

    public JPanel getControlPanel() {
        return buttonPanel;
    }
}
