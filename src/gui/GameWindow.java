package gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 * Окно игры внутри главного окна.
 */
public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;

    /**
     * Конструктор класса GameWindow.
     * Создает окно игры с визуализатором игры.
     */
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
