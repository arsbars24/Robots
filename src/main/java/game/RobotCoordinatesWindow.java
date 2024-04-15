package game;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * Окно для отображения текущих координат робота.
 */
public class RobotCoordinatesWindow extends JInternalFrame implements Observer {
    private JLabel coordinatesLabel;
    private final RobotModel robotModel;

    public RobotCoordinatesWindow(RobotModel robotModel) {
        this.robotModel = robotModel;
        robotModel.addObserver(this);

        setTitle("Координаты робота");
        setSize(200, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        coordinatesLabel = new JLabel();
        coordinatesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(coordinatesLabel, BorderLayout.CENTER);

        setLocation(800, 0);
        setVisible(true);
    }
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof RobotModel) {
            RobotModel model = (RobotModel) o;
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            String x = decimalFormat.format(model.getPositionX());
            String y = decimalFormat.format(model.getPositionY());
            coordinatesLabel.setText("X: " + x  + ",  Y: " + y);
        }
    }
    //TODO добавить в statemanger это окно
}
