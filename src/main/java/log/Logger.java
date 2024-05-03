package log;

/**
 * Класс Logger предоставляет методы удобства для протоколирования отладочных и ошибочных сообщений.
 */
public final class Logger {

    private static final LogWindowSource defaultLogSource;

    static {
        defaultLogSource = new LogWindowSource(100); // 100 - размер листа протоколирования
    }

    private Logger() {
    }

    /**
     * Протоколирует отладочное сообщение.
     *
     * @param strMessage Отладочное сообщение для протоколирования.
     */
    public static void debug(String strMessage) {
        defaultLogSource.append(LogLevel.Debug, strMessage);
    }

    /**
     * Протоколирует сообщение об ошибке.
     *
     * @param strMessage Сообщение об ошибке для протоколирования.
     */
    public static void error(String strMessage) {
        defaultLogSource.append(LogLevel.Error, strMessage);
    }

    /**
     * Получает источник протоколирования по умолчанию, используемый Logger.
     *
     * @return Экземпляр LogWindowSource по умолчанию.
     */
    public static LogWindowSource getDefaultLogSource() {
        return defaultLogSource;
    }
}
