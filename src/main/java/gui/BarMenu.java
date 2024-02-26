package gui;

import log.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

/**
 * Панель меню приложения, содержащая различные команды и настройки.
 */
public class BarMenu {
    MainApplicationFrame mainFrame;

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
     * @return Строку меню
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
     * @return Строку меню
     */
    private JMenu createMenu(String text, int key, String textDescription, List<JMenuItem> items) {
        JMenu menu = createMenu(text, key, textDescription, items.get(0));
        for (int i = 1; i < items.size(); i++) {
            menu.add(items.get(i));
        }
        return menu;
    }

    /**
     * Метод создания меню выхода из приложения
     * @return Меню выхода
     */
    private JMenu createExitMenu() {
        JMenuItem exitMenuItem = createItem("Выход", KeyEvent.VK_X,
                (event) -> mainFrame.confirmExit());
        return createMenu("Выход", KeyEvent.VK_X,
                "Закрыть приложение", exitMenuItem);
    }

}