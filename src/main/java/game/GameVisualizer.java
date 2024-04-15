package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;

/**
 * Панель для визуализации игровых объектов.
 */
public class GameVisualizer extends JPanel implements Observer {
    private final RobotModel robotModel;

    public GameVisualizer(RobotModel robotModel) {
        this.robotModel = robotModel;
        robotModel.addObserver(this);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPosition(e.getPoint());
                moveRobot();
                repaint();
            }
        });
    }

    protected void setTargetPosition(Point p) {
        robotModel.setTargetPosition((int) Math.round(p.getX()),(int) Math.round(p.getY()));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d,
                (int) Math.round(robotModel.getPositionX()),
                (int) Math.round(robotModel.getPositionY()),
                robotModel.getDirection());
        drawTarget(g2d,
                robotModel.getTargetPositionX(),
                robotModel.getTargetPositionY());
    }

    protected void moveRobot() {
        Thread moveThread = new Thread(() -> {
            while (!robotModel.isAtTarget()) {
                robotModel.moveRobot();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        moveThread.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        int robotCenterX = (int) Math.round(robotModel.getPositionX());
        int robotCenterY = (int) Math.round(robotModel.getPositionY());
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
