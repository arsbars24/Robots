package locale;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Класс отвечающий за локализацию
 */
public class LocaleManager {

    /**
     * Перечисление доступных языков
     */
    public enum Language {
        EN, // Английский
        RU  // Русский
    }

    /**
     * Хранит текущий язык
     */
    private static Language currentLanguage;

    /**
     * Возвращает ресурсы со связанным текущим языком.
     * Если текущий язык не установлен, возвращает ресурсы на русском языке по умолчанию.
     *
     * @return ресурсы на нужном языке
     */
    public static ResourceBundle getCurrentResource(Language userLan) {
        return switch (userLan) {
            case EN -> ResourceBundle.getBundle(
                    "locales/messages_en",
                    new Locale("en", "EN"));
            case RU -> ResourceBundle.getBundle(
                    "locales/messages_ru",
                    new Locale("ru", "RU"));
            case null -> ResourceBundle.getBundle("locales/messages_ru",
                    new Locale("ru", "RU"));
        };
    }

    /**
     * Возвращает текущий язык.
     *
     * @return текущий язык
     */
    public static Language getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * Устанавливает текущий язык и сохраняет его в файле настроек.
     *
     * @param userLan выбранный пользователем язык
     */
    public static synchronized void setCurrentLanguage(Language userLan) {
        currentLanguage = userLan;
        saveSetting(currentLanguage);
    }

    /**
     * Сохраняет текущий язык в файле настроек.
     *
     * @param language язык для сохранения
     */
    private static void saveSetting(Language language) {
        Properties properties = new Properties();
        properties.setProperty("language", language.toString());

        try (OutputStream outputStream = new FileOutputStream(
                "src/main/resources/app.properties")) {
            properties.store(outputStream, "Application Settings");
        } catch (IOException e) {
            e.printStackTrace(); // Обработка ошибок записи файла
        }
    }

    /**
     * Загружает выбранный язык из файла настроек.
     *
     * @return выбранный язык из файла настроек
     */
    public static Language loadSetting() {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(
                "src/main/resources/app.properties")) {
            properties.load(inputStream);
            String languageString = properties.getProperty("language");
            if (languageString != null) {
                return Language.valueOf(languageString);
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace(); // Обработка ошибок загрузки файла
        }
        // Если произошла ошибка или файл не содержит языка, возвращаем язык по умолчанию
        return Language.RU;
    }
}
