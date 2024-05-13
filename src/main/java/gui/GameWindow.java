package gui;

import game.GameVisualizer;
import game.RobotModel;
import locale.LocaleManager;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ResourceBundle;

/**
 * Окно игры внутри главного окна.
 */
public class GameWindow extends JInternalFrame {
    private final GameVisualizer m_visualizer;
    private final RobotModel robotModel;
    private final ResourceBundle resources;

    /**
     * Конструктор класса GameWindow.
     * Создает окно игры с визуализатором игры.
     */
    public GameWindow(RobotModel robotModel) {
        super();
        this.robotModel = robotModel;
        m_visualizer = new GameVisualizer(this.robotModel);
        resources = LocaleManager.getCurrentResource(
                LocaleManager.getCurrentLanguage());
        // Получаем ресурсы для текущего языка

        // Локализация заголовка окна
        setTitle(resources.getString("game_window_title"));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
