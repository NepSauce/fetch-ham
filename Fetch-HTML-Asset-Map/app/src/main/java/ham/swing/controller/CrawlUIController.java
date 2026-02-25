package ham.swing.controller;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ham.hamcrawler.HAMEntry;
import ham.hamcrawler.model.CrawlCallbacks;
import ham.hamcrawler.model.CrawlMode;
import ham.hamcrawler.model.CrawlOptions;
import ham.hamcrawler.model.CrawlStatus;
import ham.swing.panels.URLPanels.URLSwitchPanel;

public class CrawlUIController {
    private static final int HARDCODED_THREAD_COUNT = 4;

    private final HAMEntry hamEntry;
    private final JTextField urlField;
    private final JComboBox<String> modeComboBox;
    private final URLSwitchPanel switchPanel;
    private final JButton mapButton;
    private final JButton stopButton;
    private final JPanel buttonPanel;

    public CrawlUIController(
            HAMEntry hamEntry,
            JTextField urlField,
            JComboBox<String> modeComboBox,
            URLSwitchPanel switchPanel,
            JButton mapButton,
            JButton stopButton,
            JPanel buttonPanel
    ) {
        this.hamEntry = hamEntry;
        this.urlField = urlField;
        this.modeComboBox = modeComboBox;
        this.switchPanel = switchPanel;
        this.mapButton = mapButton;
        this.stopButton = stopButton;
        this.buttonPanel = buttonPanel;

        this.mapButton.addActionListener(e -> startCrawl());
        this.stopButton.addActionListener(e -> stopCrawl());

        updateUiState(false);
    }

    private void startCrawl() {
        if (hamEntry.isRunning()) {
            return;
        }

        String startUrl = sanitizeUrl(urlField.getText());
        if (startUrl == null) {
            return;
        }

        CrawlOptions options = new CrawlOptions(
                startUrl,
                getMode(),
                switchPanel.getRobotSwitch().isSelected(),
                switchPanel.getRulesSwitch().isSelected(),
                switchPanel.getExtractNonHtmlSwitch().isSelected(),
                switchPanel.getMetadataExtractionSwitch().isSelected(),
                HARDCODED_THREAD_COUNT
        );

        hamEntry.startCrawl(options, new CrawlCallbacks() {
            @Override
            public void onStarted(CrawlOptions startedOptions) {
                System.out.println(
                        "[HAM] Crawl started | url=" + startedOptions.startUrl()
                                + " | mode=" + startedOptions.mode()
                                + " | threads=" + startedOptions.threadCount()
                );
                runOnEdt(() -> updateUiState(true));
            }

            @Override
            public void onProgress(CrawlStatus status) {
                if (status.currentUrl() != null) {
                    System.out.println(
                            "[HAM] Crawling | url=" + status.currentUrl()
                                    + " | visited=" + status.visitedCount()
                                    + " | discovered=" + status.discoveredCount()
                                    + " | queued=" + status.queuedCount()
                                    + " | active=" + status.activeWorkers()
                    );
                }
            }

            @Override
            public void onCompleted(CrawlStatus finalStatus) {
                System.out.println(
                        "[HAM] Crawl completed | visited=" + finalStatus.visitedCount()
                                + " | discovered=" + finalStatus.discoveredCount()
                                + " | queued=" + finalStatus.queuedCount()
                );
                runOnEdt(() -> updateUiState(false));
            }

            @Override
            public void onStopped(CrawlStatus finalStatus) {
                System.out.println(
                        "[HAM] Crawl stopped | visited=" + finalStatus.visitedCount()
                                + " | discovered=" + finalStatus.discoveredCount()
                                + " | queued=" + finalStatus.queuedCount()
                );
                runOnEdt(() -> updateUiState(false));
            }

            @Override
            public void onError(String message, Throwable throwable) {
                if (throwable != null) {
                    System.out.println("[HAM] Crawl error | " + message + " | " + throwable.getMessage());
                } else {
                    System.out.println("[HAM] Crawl error | " + message);
                }
                runOnEdt(() -> updateUiState(false));
            }
        });
    }

    private void stopCrawl() {
        if (!hamEntry.isRunning()) {
            return;
        }

        hamEntry.stopCrawl();
        updateUiState(false);
    }

    private CrawlMode getMode() {
        String selected = String.valueOf(modeComboBox.getSelectedItem());
        if ("Contained".equalsIgnoreCase(selected)) {
            return CrawlMode.CONTAINED;
        }
        return CrawlMode.SEED;
    }

    private String sanitizeUrl(String rawUrl) {
        if (rawUrl == null) {
            return null;
        }

        String url = rawUrl.trim();
        if (url.isBlank()) {
            return null;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        return url;
    }

    private void updateUiState(boolean running) {
        mapButton.setEnabled(!running);
        stopButton.setVisible(running);
        stopButton.setEnabled(running);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void runOnEdt(Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
            return;
        }

        SwingUtilities.invokeLater(runnable);
    }
}
