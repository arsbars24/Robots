package gui;

import game.GameVisualizer;
import game.RobotModel;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Окно игры внутри главного окна.
 */
public class GameWindow extends JInternalFrame {
    private final GameVisualizer m_visualizer;
    private final RobotModel robotModel;

    /**
     * Конструктор класса GameWindow.
     * Создает окно игры с визуализатором игры.
     */
    public GameWindow(RobotModel robotModel) {
        super("Игровое поле", true, true, true, true);
        this.robotModel = robotModel;
        m_visualizer = new GameVisualizer(this.robotModel);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
