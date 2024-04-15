package gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import game.RobotCoordinatesWindow;
import game.RobotModel;
import log.Logger;
import state.StateManager;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Главное окно приложения, содержащее в себе рабочую область и панель меню.
 */
public class MainApplicationFrame extends JFrame{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private LogWindow logWindow;
    private GameWindow gameWindow;
    private RobotCoordinatesWindow robotCoordinatesWindow;
    private final StateManager stateManager;
    private final RobotModel robotModel;

    private final ResourceBundle bundle = ResourceBundle
            .getBundle("messages",
                    new Locale("ru", "RU"));

    /**
     * Конструктор класса MainApplicationFrame.
     * Создает экземпляр главного окна приложения.
     */
    public MainApplicationFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2
        );

        setContentPane(desktopPane);

        logWindow = createLogWindow(); // Создаем logWindow
        addWindow(logWindow);

        robotModel = new RobotModel(100, 100);

        gameWindow = new GameWindow(robotModel); // Создаем gameWindow
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        robotCoordinatesWindow = new RobotCoordinatesWindow(robotModel);
        addWindow(robotCoordinatesWindow);

        stateManager = new StateManager(this, logWindow, gameWindow);

        BarMenu barMenu = new BarMenu(this);
        setJMenuBar(barMenu.generateMenuBar());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
        stateManager.restoreState();
    }

    /**
     * Создает окно журнала событий.
     * @return Экземпляр окна журнала событий.
     */
    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    /**
     * Добавляет внутреннее окно в главное окно приложения.
     * @param frame Внутреннее окно для добавления.
     */
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * Метод для подтверждения выхода
     */
    protected void confirmExit() {
        Object[] choices = {bundle.getString("quit"), bundle.getString("cancel")};
        Object defaultChoice = choices[0];
        int confirmed = JOptionPane.showOptionDialog(null,
                bundle.getString("quitQuestion"),
                bundle.getString("quitTitle"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                defaultChoice);

        if (confirmed == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(1);
            stateManager.saveState();
        }
    }
}