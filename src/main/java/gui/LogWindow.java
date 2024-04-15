package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

/**
 * Окно протокола работы приложения.
 */
public class LogWindow extends JInternalFrame implements LogChangeListener
{
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;

    /**
     * Конструктор класса LogWindow.
     * Создает окно протокола работы приложения.
     *
     * @param logSource Источник данных для протокола.
     */
    public LogWindow(LogWindowSource logSource) 
    {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    /**
     * Обновляет содержимое протокола.
     */
    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    /**
     * Вызывается при изменении протокола.
     * Обновляет содержимое протокола работы приложения на графическом интерфейсе.
     * Данный метод вызывается асинхронно на главном потоке событий (EDT) с помощью
     * метода {@link EventQueue#invokeLater(Runnable)}, чтобы обеспечить безопасность
     * работы с графическим интерфейсом Swing.
     */
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
