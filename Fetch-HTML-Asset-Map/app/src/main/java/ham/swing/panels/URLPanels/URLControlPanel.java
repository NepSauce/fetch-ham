package ham.swing.panels.URLPanels;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class URLControlPanel {
    private JPanel buttonPanel;

    public URLControlPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton robotButton = new JButton("Fetch Robots.txt");

        buttonPanel.add(robotButton);
    }

    public JPanel getControlPanel() {
        return buttonPanel;
    }
}
