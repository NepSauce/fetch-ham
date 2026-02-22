package ham.swing.panels.URLPanels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

@SuppressWarnings("FieldMayBeFinal")
public class URLSwitchPanel {
    private JToggleButton robotSwitch;
    private JToggleButton rulesSwitch;
    private JToggleButton extractNonHtmlSwitch;
    private JToggleButton metadataExtractionSwitch;
    private JPanel switchPanel;
    
    public URLSwitchPanel() {
        switchPanel = new JPanel();
        switchPanel.setLayout(new BoxLayout(switchPanel, BoxLayout.X_AXIS));
        switchPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        robotSwitch = createSwitchButton();
        rulesSwitch = createSwitchButton();
        extractNonHtmlSwitch = createSwitchButton();
        metadataExtractionSwitch = createSwitchButton();

        JPanel leftColumn = createColumnPanel();
        leftColumn.add(createSwitchRow("Respect Robots.txt", robotSwitch));
        leftColumn.add(createSwitchRow("Enable Rules", rulesSwitch));

        JPanel rightColumn = createColumnPanel();
        rightColumn.add(createSwitchRow("Extract Non-HTML", extractNonHtmlSwitch));
        rightColumn.add(createSwitchRow("Extract Metadata", metadataExtractionSwitch));

        robotSwitch.setSelected(true);

        switchPanel.add(leftColumn);
        switchPanel.add(rightColumn);
    }

    private JPanel createColumnPanel() {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setAlignmentY(Component.CENTER_ALIGNMENT);
        return column;
    }

    private JPanel createSwitchRow(String text, JToggleButton switchButton) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField textBox = new JTextField(text);
        setComponentSize(textBox, new Dimension(130, 17));
        textBox.setEditable(false);

        row.add(textBox);
        row.add(switchButton);
        return row;
    }

    private JToggleButton createSwitchButton() {
        JToggleButton switchButton = new JToggleButton();
        setComponentSize(switchButton, new Dimension(25, 17));
        switchButton.setFocusPainted(true);
        switchButton.setOpaque(true);
        switchButton.setBackground(new Color(210, 210, 210));

        switchButton.addActionListener(e -> {
            boolean enabled = switchButton.isSelected();
            switchButton.setBackground(enabled ? new Color(125, 198, 110) : new Color(210, 210, 210));
        });

        return switchButton;
    }

    private void setComponentSize(Component component, Dimension dimension) {
        component.setPreferredSize(dimension);
        component.setMaximumSize(dimension);
        component.setMinimumSize(dimension);
    }

    public JPanel getSwitchPanel() {
        return switchPanel;
    }

    public JToggleButton getRobotSwitch() {
        return robotSwitch;
    }

    public JToggleButton getRulesSwitch() {
        return rulesSwitch;
    }

    public JToggleButton getExtractNonHtmlSwitch() {
        return extractNonHtmlSwitch;
    }

    public JToggleButton getMetadataExtractionSwitch() {
        return metadataExtractionSwitch;
    }
    
}
