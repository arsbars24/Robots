package state;

import gui.GameWindow;
import gui.*;

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

    /**
     * Создает объект StateManager с указанным главным фреймом, окном журнала и окном игры.
     *
     * @param mainFrame  главный фрейм приложения
     * @param logWindow  окно журнала
     * @param gameWindow окно игры
     */
    public StateManager(MainApplicationFrame mainFrame, LogWindow logWindow, GameWindow gameWindow){
        this.mainFrame = mainFrame;
        this.logWindow = logWindow;
        this.gameWindow = gameWindow;
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

        // Сохранение состояния окна игры
        state.put("gameWindowX", Integer.toString(gameWindow.getX()));
        state.put("gameWindowY", Integer.toString(gameWindow.getY()));
        state.put("gameWindowWidth", Integer.toString(gameWindow.getWidth()));
        state.put("gameWindowHeight", Integer.toString(gameWindow.getHeight()));

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
            e.printStackTrace();
        }
        if (state != null) {
            // Восстановление состояния главного окна
            mainFrame.setBounds(
                    Integer.parseInt(state.get("mainFrameX")),
                    Integer.parseInt(state.get("mainFrameY")),
                    Integer.parseInt(state.get("mainFrameWidth")),
                    Integer.parseInt(state.get("mainFrameHeight"))
            );

            // Восстановление состояния окна протокола
            logWindow.setBounds(
                    Integer.parseInt(state.get("logWindowX")),
                    Integer.parseInt(state.get("logWindowY")),
                    Integer.parseInt(state.get("logWindowWidth")),
                    Integer.parseInt(state.get("logWindowHeight"))
            );

            // Восстановление состояния окна игры
            gameWindow.setBounds(
                    Integer.parseInt(state.get("gameWindowX")),
                    Integer.parseInt(state.get("gameWindowY")),
                    Integer.parseInt(state.get("gameWindowWidth")),
                    Integer.parseInt(state.get("gameWindowHeight"))
            );
        }
    }
}