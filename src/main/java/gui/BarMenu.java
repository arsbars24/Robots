package gui;

import log.Logger;
import locale.LocaleManager;
import state.StateManager;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Класс для создания меню приложения.
 */
public class BarMenu {
    MainApplicationFrame mainFrame;
    StateManager stateManager;
    private final ResourceBundle resources;

    /**
     * Конструктор класса BarMenu.
     *
     * @param mainFrame    Главное окно приложения.
     * @param stateManager Менеджер состояний приложения.
     */
    public BarMenu(MainApplicationFrame mainFrame, StateManager stateManager) {
        this.mainFrame = mainFrame;
        this.stateManager = stateManager;
        resources = LocaleManager.getCurrentResource(
                LocaleManager.getCurrentLanguage()); // Получаем ресурсы для текущего языка
    }

    /**
     * Генерирует строку меню.
     *
     * @return Сгенерированное меню.
     */
    public JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(lookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createExitMenu());
        menuBar.add(createLocaleMenu());
        return menuBar;
    }

    /**
     * Создает меню внешнего вида.
     *
     * @return Меню внешнего вида.
     */
    private JMenu lookAndFeelMenu() {
        return createMenu(resources.getString("look_and_feel_menu_title"),
                KeyEvent.VK_V,
                resources.getString("look_and_feel_menu_description"),
                Arrays.asList(
                        createItem(resources.getString("system_look_and_feel_menu_item"), KeyEvent.VK_S, (event) -> {
                            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            mainFrame.invalidate();
                        }),
                        createItem(resources.getString("cross_platform_look_and_feel_menu_item"), KeyEvent.VK_S, (event) -> {
                            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                            mainFrame.invalidate();
                        })));
    }

    /**
     * Создает меню тестов.
     *
     * @return Меню тестов.
     */
    private JMenu createTestMenu() {
        return createMenu(resources.getString("test_menu_title"),
                KeyEvent.VK_T,
                resources.getString("test_menu_description"),
                createItem(resources.getString("log_message_menu_item"), KeyEvent.VK_S, (event)
                        -> Logger.debug(resources.getString("log_message_text")))); // Используем локализованный текст
    }

    /**
     * Устанавливает внешний вид.
     *
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
     * Создает пункт меню.
     *
     * @param text           Текст пункта меню.
     * @param key            Клавиша быстрого доступа.
     * @param actionListener Обработчик события.
     * @return Пункт меню.
     */
    private JMenuItem createItem(String text, int key, ActionListener actionListener) {
        JMenuItem jMenuItem = new JMenuItem(text, key);
        jMenuItem.addActionListener(actionListener);
        return jMenuItem;
    }

    /**
     * Создает меню.
     *
     * @param text           Текст меню.
     * @param key            Клавиша быстрого доступа.
     * @param textDescription Описание меню.
     * @param item           Пункт меню.
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
     * Создает меню с несколькими пунктами.
     *
     * @param text           Текст меню.
     * @param key            Клавиша быстрого доступа.
     * @param textDescription Описание меню.
     * @param items          Пункты меню.
     * @return Меню.
     */
    private JMenu createMenu(String text, int key, String textDescription, List<JMenuItem> items) {
        JMenu menu = createMenu(text, key, textDescription, items.get(0));
        for (int i = 1; i < items.size(); i++) {
            menu.add(items.get(i));
        }
        return menu;
    }

    /**
     * Создает меню выхода из приложения.
     *
     * @return Меню выхода из приложения.
     */
    private JMenu createExitMenu() {
        JMenuItem exitMenuItem = createItem(resources.getString("exit_menu_item"), KeyEvent.VK_X,
                (event) -> mainFrame.confirmExit());
        return createMenu(resources.getString("exit_menu_title"), KeyEvent.VK_X,
                resources.getString("exit_menu_description"), exitMenuItem);
    }

    /**
     * Создает меню смены языка.
     *
     * @return Меню смены языка.
     */
    private JMenu createLocaleMenu() {
        JMenu localeMenu = new JMenu(resources.getString("language_menu_title"));
        localeMenu.setMnemonic(KeyEvent.VK_L);

        JMenuItem russianMenuItem = createLanguageMenuItem(resources.getString("russian_menu_item"), LocaleManager.Language.RU);
        JMenuItem englishMenuItem = createLanguageMenuItem(resources.getString("english_menu_item"), LocaleManager.Language.EN);

        localeMenu.add(russianMenuItem);
        localeMenu.add(englishMenuItem);

        return localeMenu;
    }

    /**
     * Создает пункт меню для смены языка.
     *
     * @param languageName Имя языка.
     * @param language     Язык.
     * @return Пункт меню для смены языка.
     */
    private JMenuItem createLanguageMenuItem(String languageName, LocaleManager.Language language) {
        JMenuItem menuItem = new JMenuItem(languageName);
        menuItem.addActionListener(e -> {
            LocaleManager.setCurrentLanguage(language);
            stateManager.saveState();
            mainFrame.dispose();
            SwingUtilities.invokeLater(() -> {
                MainApplicationFrame frame = new MainApplicationFrame();
                frame.setVisible(true);
            });
        });
        return menuItem;
    }
}
