package ham.swing.panels.URLPanels;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("FieldMayBeFinal")
public class URLStopButton {
    private JPanel stopButtonPanel;
    private JButton stopButton;

    public URLStopButton() {
        stopButtonPanel = new JPanel();

        stopButtonPanel.setLayout(new BoxLayout(stopButtonPanel, BoxLayout.X_AXIS));
        stopButtonPanel.setBackground(Color.WHITE);
        stopButtonPanel.setPreferredSize(new java.awt.Dimension(100, 34));
        stopButtonPanel.setMaximumSize(new java.awt.Dimension(100, 34));
        stopButtonPanel.setMinimumSize(new java.awt.Dimension(100, 34));
        stopButtonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        stopButton = new JButton("Stop");
        stopButton.setPreferredSize(new java.awt.Dimension(100, 34));
        stopButton.setMaximumSize(new java.awt.Dimension(100, 34));
        stopButton.setMinimumSize(new java.awt.Dimension(100, 34));
        stopButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        stopButton.setVisible(false);

        stopButtonPanel.add(stopButton);
    }

    public JPanel getStopButtonPanel() {
        return stopButtonPanel;
    }

    public JButton getStopButton() {
        return stopButton;
    }
}
