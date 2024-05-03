package log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс LogWindowSource представляет источник протоколирования, который хранит и управляет сообщениями лога.
 */
public class LogWindowSource {
    private final int m_iQueueLength;
    private final LinkedList<LogEntry> m_messages;
    private final List<LogChangeListener> m_listeners;

    /**
     * Конструктор для создания объекта LogWindowSource с указанным размером очереди.
     *
     * @param iQueueLength Размер очереди протоколирования.
     */
    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new LinkedList<>();
        m_listeners = new ArrayList<>();
    }

    /**
     * Регистрирует слушателя изменений протокола.
     *
     * @param listener Слушатель изменений протокола.
     */
    public synchronized void registerListener(LogChangeListener listener) {
        m_listeners.add(listener);
    }

    /**
     * Отменяет регистрацию слушателя изменений протокола.
     *
     * @param listener Слушатель изменений протокола.
     */
    public synchronized void unregisterListener(LogChangeListener listener) {
        m_listeners.remove(listener);
    }

    /**
     * Добавляет новую запись в лог с указанным уровнем и сообщением.
     *
     * @param logLevel   Уровень протоколирования.
     * @param strMessage Сообщение для протоколирования.
     */
    public synchronized void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        if (size() >= m_iQueueLength) {
            m_messages.removeFirst();
        }
        m_messages.addLast(entry);
        notifyListeners();
    }

    private void notifyListeners() {
        for (LogChangeListener listener : m_listeners) {
            listener.onLogChanged();
        }
    }

    /**
     * Возвращает текущий размер лога.
     *
     * @return Размер лога.
     */
    public synchronized int size() {
        return m_messages.size();
    }

    /**
     * Возвращает итерируемую коллекцию сообщений лога в указанном диапазоне.
     *
     * @param startFrom Индекс начала диапазона.
     * @param count     Количество сообщений.
     * @return Коллекция сообщений в указанном диапазоне.
     */
    public synchronized Iterable<LogEntry> range(int startFrom, int count) {
        int toIndex = Math.min(startFrom + count, m_messages.size());
        return new ArrayList<>(m_messages.subList(startFrom, toIndex));
    }

    /**
     * Возвращает итерируемую коллекцию всех сообщений лога.
     *
     * @return Коллекция всех сообщений лога.
     */
    public synchronized Iterable<LogEntry> all() {
        return List.copyOf(m_messages);
    }
}
