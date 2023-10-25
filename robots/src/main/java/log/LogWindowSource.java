package main.java.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogWindowSource {
    private final int m_iQueueLength;

    private final ArrayList<LogEntry> m_messages;
    private final CopyOnWriteArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new ArrayList<>(iQueueLength);
        m_listeners = new CopyOnWriteArrayList<>();
    }

    public void registerListener(LogChangeListener listener) {
        m_listeners.add(listener);
        m_activeListeners = null;
    }

    public void unregisterListener(LogChangeListener listener) {
        m_listeners.remove(listener);
        m_activeListeners = null;
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        if (size() >= m_iQueueLength) m_messages.remove(0);
        synchronized (this) {
            m_messages.add(entry);
        }
        LogChangeListener[] activeListeners = m_activeListeners;
        if (activeListeners == null) {
            synchronized (m_listeners) {
                if (m_activeListeners == null) {
                    activeListeners = m_listeners.toArray(new LogChangeListener[0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners) {
            listener.onLogChanged();
        }
    }

    public synchronized int size() {
        return m_messages.size();
    }

    public synchronized Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= size()) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, size());
        return m_messages.subList(startFrom, indexTo);
    }

    public synchronized Iterable<LogEntry> all() {
        return m_messages;
    }
}
