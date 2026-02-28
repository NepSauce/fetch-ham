package ham.swing.panels;


import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class LogBasePanel {
    private JTextArea logArea;

    public LogBasePanel() {
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.

        JLabel logLabel = new JLabel("Crawl Log:");
        logLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        
    }   
    
}
    