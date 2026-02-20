package ham.swing.panels;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("FieldMayBeFinal")
public class URLButtonPanel {
    private JPanel buttonPanel;

    public URLButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton robotButton = new JButton("Fetch Robots.txt");

        buttonPanel.add(robotButton);
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }
}
