package gui;

import game.RobotModel;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * Окно для отображения текущих координат робота.
 */
public class RobotCoordinatesWindow extends JInternalFrame implements Observer {
    private final JLabel coordinatesLabel;

    /**
     * Конструктор класса RobotCoordinatesWindow.
     * Создает окно для отображения текущих координат робота.
     *
     * @param robotModel Модель робота, которую необходимо отслеживать.
     */
    public RobotCoordinatesWindow(RobotModel robotModel) {
        robotModel.addObserver(this);

        setTitle("Координаты робота");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        coordinatesLabel = new JLabel();
        coordinatesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(coordinatesLabel, BorderLayout.CENTER);

        setSize(200, 100);
        setLocation(800, 0);
        setVisible(true);
    }

    /**
     * Обновляет отображаемые координаты робота при изменении состояния модели.
     *
     * @param o   Наблюдаемый объект.
     * @param arg Аргументы изменения состояния (не используется).
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof RobotModel model) {
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            String x = decimalFormat.format(model.getPositionX());
            String y = decimalFormat.format(model.getPositionY());
            coordinatesLabel.setText("X: " + x  + ",  Y: " + y);
        }
    }
}
