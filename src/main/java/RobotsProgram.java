import gui.MainApplicationFrame;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Основной класс программы для запуска приложения Robots.
 */
public class RobotsProgram
{
  /**
   * Точка входа в приложение.
   *
   * @param args Аргументы командной строки (не используются).
   */
    public static void main(String[] args) {
      try {
        // Установка внешнего вида Nimbus
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      } catch (Exception e) {
        e.printStackTrace();
      }
      // Запуск GUI приложения на главном потоке событий
      SwingUtilities.invokeLater(() -> {
        MainApplicationFrame frame = new MainApplicationFrame();
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
      });
    }}
