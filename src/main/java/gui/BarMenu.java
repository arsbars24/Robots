package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Панель меню приложения, содержащая различные команды и настройки.
 */
public class BarMenu {
    MainApplicationFrame mainFrame;
    private final ResourceBundle bundle = ResourceBundle
            .getBundle("resources/messages",
                    new Locale("ru", "RU"));

    /**
     * Конструктор класса BarMenu.
     * Создает панель меню приложения.
     * @param mainFrame Главное окно приложения.
     */
    public BarMenu(MainApplicationFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * Генерирует панель меню.
     * @return Панель меню приложения.
     */
    public JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(lookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createExitMenu());
        return menuBar;
    }

    /**
     * Создает меню для выбора режима отображения.
     * @return Меню выбора режима отображения.
     */
    private JMenu lookAndFeelMenu() {
        return createMenu("Режим отображения",
                KeyEvent.VK_V,
                "Управление режимом отображения приложения",
                Arrays.asList(
                        createItem("Системная схема", KeyEvent.VK_S, (event) -> {
                            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            mainFrame.invalidate();
                        }),
                        createItem("Универсальная схема", KeyEvent.VK_S, (event) -> {
                            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                            mainFrame.invalidate();
                        })));
    }

    /**
     * Создает тестовое меню.
     * @return Тестовое меню.
     */
    private JMenu createTestMenu() {
        return createMenu("Тесты",
                KeyEvent.VK_T,
                "Тестовые команды",
                createItem("Сообщение в лог", KeyEvent.VK_S, (event)
                        -> Logger.debug("Новая строка")));
    }

    /**
     * Устанавливает внешний вид приложения.
     * @param className Имя класса внешнего вида.
     */
    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(mainFrame);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
        }
    }

    /**
     * Создает элемент меню.
     * @param text Текст элемента меню.
     * @param key Клавиша для быстрого доступа.
     * @param actionListener Обработчик события элемента меню.
     * @return Элемент меню.
     */
    private JMenuItem createItem(String text, int key, ActionListener actionListener) {
        JMenuItem jMenuItem = new JMenuItem(text, key);
        jMenuItem.addActionListener(actionListener);
        return jMenuItem;
    }

    /**
     * Создает меню.
     * @param text Текст меню.
     * @param key Клавиша для быстрого доступа.
     * @param textDescription Описание меню.
     * @param item Элемент меню.
     * @return Меню.
     */
    private JMenu createMenu(String text, int key, String textDescription, JMenuItem item) {
        JMenu menu = new JMenu(text);
        menu.setMnemonic(key);
        menu.getAccessibleContext()
                .setAccessibleDescription(textDescription);
        menu.add(item);
        return menu;
    }
    /**
     * Создает меню.
     * @param text Текст меню.
     * @param key Клавиша для быстрого доступа.
     * @param textDescription Описание меню.
     * @param items Элементы меню.
     * @return Меню.
     */
    private JMenu createMenu(String text, int key, String textDescription, List<JMenuItem> items) {
        JMenu menu = createMenu(text, key, textDescription, items.get(0));
        for (int i = 1; i < items.size(); i++) {
            menu.add(items.get(i));
        }
        return menu;
    }

    // Метод создания меню для выхода из приложения
    //TODO javadoc
    private JMenu createExitMenu() {
        JMenuItem exitMenuItem = createItem("Выход", KeyEvent.VK_X, (event) -> confirmExit());
        return createMenu("Выход", KeyEvent.VK_X, "Закрыть приложение", exitMenuItem);
    }

    // Метод для подтверждения выхода с запросом на русском языке
    //TODO javadoc
    private void confirmExit() {
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
            //WindowEvent closeEvent = new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING);
            //Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
            System.exit(0);
        }
    }


}