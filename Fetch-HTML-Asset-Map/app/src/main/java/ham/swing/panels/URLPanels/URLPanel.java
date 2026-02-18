package ham.swing.panels.URLPanels;

import javax.swing.JTextArea;

@SuppressWarnings("FieldMayBeFinal")
public class URLPanel {
    private JTextArea urlTextArea;

    public URLPanel() {
        urlTextArea = new JTextArea();
        urlTextArea.setLineWrap(true);
    }

    public JTextArea getURLPanel() {
        return urlTextArea;
    }
}
