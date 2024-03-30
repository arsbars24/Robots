package state;

/**
 * Интерфейс WindowState определяет методы для сохранения и восстановления состояния окна.
 */
public interface WindowState {
    void saveState();
    void restoreState();
}