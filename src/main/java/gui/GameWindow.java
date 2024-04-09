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

    /**
     * Конструктор класса GameWindow.
     * Создает окно игры с визуализатором игры.
     */
    public GameWindow() {
        super("Игровое поле", true, true, true, true);

        // Передаем начальные координаты робота при создании экземпляра RobotModel
        RobotModel robotModel = new RobotModel(100, 100); // Например, начальные координаты (100, 100)

        m_visualizer = new GameVisualizer(robotModel);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
