package gui;

import interfaces.MyFrame;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import log.Logger;
import model.FrameType;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

public class LogWindow extends MyFrame implements LogChangeListener {
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;
    private final LocalizationManager localizator;

    public LogWindow(LogWindowSource logSource, LocalizationManager localizator) {
        super(localizator.getString("window.log"), true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        updateLogContent();
        this.localizator = localizator;

        setMinimumSize(this.getSize());
        pack();
        Logger.debug(localizator.getString("window.log.first_message"));
    }

    public void updateLocale() {
        setTitle(localizator.getString("window.log"));
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    public void toDefaultState() {
        setLocation(10, 10);
        setSize(200, 500);
        isIcon = false;
    }

    public FrameType getId() {
        return FrameType.LOG_WINDOW;
    }
}
