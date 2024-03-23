package state;

import java.util.Map;

/**
 * Интерфейс для работы с состоянием
 */
public interface WindowState {
    void saveState();
    void restoreState();
}