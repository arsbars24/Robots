package state;

import gui.GameWindow;
import gui.*;

import java.beans.PropertyVetoException;
import java.io.*;
import java.util.*;

/**
 * Класс StateManager отвечает за управление состоянием различных окон в приложении.
 * Он реализует интерфейс WindowState для предоставления методов сохранения и
 * восстановления состояний окон.
 */

public class StateManager implements WindowState {
    private final MainApplicationFrame mainFrame;
    private final LogWindow logWindow;
    private final GameWindow gameWindow;
    private final RobotCoordinatesWindow robotCoordinatesWindow;

    /**
     * Создает объект StateManager с указанным главным фреймом, окном журнала и окном игры.
     *
     * @param mainFrame  главный фрейм приложения
     * @param logWindow  окно журнала
     * @param gameWindow окно игры
     * @param robotCoordinatesWindow окно координат робота
     */
    public StateManager(MainApplicationFrame mainFrame,
                        LogWindow logWindow,
                        GameWindow gameWindow,
                        RobotCoordinatesWindow robotCoordinatesWindow){
        this.mainFrame = mainFrame;
        this.logWindow = logWindow;
        this.gameWindow = gameWindow;
        this.robotCoordinatesWindow = robotCoordinatesWindow;
    }

    /**
     * Сохраняет состояние главного фрейма, окна журнала и окна игры в файл.
     */
    @Override
    public void saveState() {
        Map<String, String> state = new HashMap<>();
        // Сохранение состояния главного окна
        state.put("mainFrameX", Integer.toString(mainFrame.getX()));
        state.put("mainFrameY", Integer.toString(mainFrame.getY()));
        state.put("mainFrameWidth", Integer.toString(mainFrame.getWidth()));
        state.put("mainFrameHeight", Integer.toString(mainFrame.getHeight()));

        // Сохранение состояния окна протокола
        state.put("logWindowX", Integer.toString(logWindow.getX()));
        state.put("logWindowY", Integer.toString(logWindow.getY()));
        state.put("logWindowWidth", Integer.toString(logWindow.getWidth()));
        state.put("logWindowHeight", Integer.toString(logWindow.getHeight()));
        state.put("logWindowIsIcon", Boolean.toString(logWindow.isIcon()));

        // Сохранение состояния окна игры
        state.put("gameWindowX", Integer.toString(gameWindow.getX()));
        state.put("gameWindowY", Integer.toString(gameWindow.getY()));
        state.put("gameWindowWidth", Integer.toString(gameWindow.getWidth()));
        state.put("gameWindowHeight", Integer.toString(gameWindow.getHeight()));
        state.put("gameWindowIsIcon", Boolean.toString(gameWindow.isIcon()));

        // Сохранение состояния окна координат робота
        state.put("robotCoordinatesWindowX", Integer.toString(robotCoordinatesWindow.getX()));
        state.put("robotCoordinatesWindowY", Integer.toString(robotCoordinatesWindow.getY()));
        state.put("robotCoordinatesWindowWidth", Integer.toString(robotCoordinatesWindow.getWidth()));
        state.put("robotCoordinatesWindowHeight", Integer.toString(robotCoordinatesWindow.getHeight()));
        state.put("robotCoordinatesWindowIsIcon", Boolean.toString(robotCoordinatesWindow.isIcon()));


        String configFilePath = System.getProperty("user.home") + File.separator + "state.dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(configFilePath))) {
            oos.writeObject(state);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Восстанавливает состояние главного фрейма, окна журнала и окна игры из файла.
     */
    @Override
    public void restoreState() {
        String configFilePath = System.getProperty("user.home") + File.separator + "state.dat";
        Map<String, String> state = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(configFilePath))) {
            state = (Map<String, String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Config is not found");
            e.printStackTrace();
        }
        if (state != null) {
            // Восстановление состояния главного окна
            if (state.containsKey("mainFrameX") && state.containsKey("mainFrameY") &&
                    state.containsKey("mainFrameWidth") && state.containsKey("mainFrameHeight")) {
                mainFrame.setBounds(
                        Integer.parseInt(state.get("mainFrameX")),
                        Integer.parseInt(state.get("mainFrameY")),
                        Integer.parseInt(state.get("mainFrameWidth")),
                        Integer.parseInt(state.get("mainFrameHeight"))
                );
            }

            // Восстановление состояния окна протокола
            if (state.containsKey("logWindowX") && state.containsKey("logWindowY") &&
                    state.containsKey("logWindowWidth") && state.containsKey("logWindowHeight")) {
                logWindow.setBounds(
                        Integer.parseInt(state.get("logWindowX")),
                        Integer.parseInt(state.get("logWindowY")),
                        Integer.parseInt(state.get("logWindowWidth")),
                        Integer.parseInt(state.get("logWindowHeight"))
                );
            }
            try {
                logWindow.setIcon(Boolean.parseBoolean(state.get("logWindowIsIcon")));
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }

            // Восстановление состояния окна игры
            if (state.containsKey("gameWindowX") && state.containsKey("gameWindowY") &&
                    state.containsKey("gameWindowWidth") && state.containsKey("gameWindowHeight")) {
                gameWindow.setBounds(
                        Integer.parseInt(state.get("gameWindowX")),
                        Integer.parseInt(state.get("gameWindowY")),
                        Integer.parseInt(state.get("gameWindowWidth")),
                        Integer.parseInt(state.get("gameWindowHeight"))
                );
            }
            try {
                gameWindow.setIcon(Boolean.parseBoolean(state.get("gameWindowIsIcon")));
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }

            // Восстановление состояния окна координат робота
            if (state.containsKey("robotCoordinatesWindowX") && state.containsKey("robotCoordinatesWindowY") &&
                    state.containsKey("robotCoordinatesWindowWidth") && state.containsKey("robotCoordinatesWindowHeight")) {
                robotCoordinatesWindow.setBounds(
                        Integer.parseInt(state.get("robotCoordinatesWindowX")),
                        Integer.parseInt(state.get("robotCoordinatesWindowY")),
                        Integer.parseInt(state.get("robotCoordinatesWindowWidth")),
                        Integer.parseInt(state.get("robotCoordinatesWindowHeight"))
                );
            }
            try {
                robotCoordinatesWindow.setIcon(Boolean.parseBoolean(state.get("robotCoordinatesWindowIsIcon")));
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
