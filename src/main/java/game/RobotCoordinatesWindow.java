package game;

import javax.swing.*;
import java.awt.*;
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

        setTitle("Robot Coordinates");
        setSize(200, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        coordinatesLabel = new JLabel();
        coordinatesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(coordinatesLabel, BorderLayout.CENTER);

        setLocation(800, 0);
        setVisible(true);
    }
    //FIXME исправить окно (не отображает координаты)
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof RobotModel) {
            RobotModel model = (RobotModel) o;
            double x = model.getPositionX();
            double y = model.getPositionY();
            coordinatesLabel.setText("X: " + x + ", Y: " + y);
        }
    }
    //TODO добавить в statemanger это окно
}
