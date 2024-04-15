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

    /**
     * Конструктор класса GameVisualizer.
     * Создает панель для визуализации игровых объектов и добавляет в нее слушателя мыши.
     *
     * @param robotModel Модель робота для визуализации.
     */
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

    /**
     * Устанавливает позицию цели на основе координат указанной точки.
     *
     * @param p Точка, в которой устанавливается цель.
     */
    protected void setTargetPosition(Point p) {
        robotModel.setTargetPosition((int) Math.round(p.getX()), (int) Math.round(p.getY()));
    }

    /**
     * Переопределенный метод отрисовки компонента.
     *
     * @param g Графический контекст для отрисовки.
     */
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

    /**
     * Перемещает робота в отдельном потоке.
     * После каждого перемещения перерисовывает панель.
     */
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

    /**
     * Обновляет отображение панели при изменении состояния модели.
     *
     * @param o   Наблюдаемый объект.
     * @param arg Аргументы изменения состояния (не используется).
     */
    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

    /**
     * Заполняет овал по указанным координатам и размерам.
     *
     * @param g       Графический контекст для отрисовки.
     * @param centerX Координата X центра овала.
     * @param centerY Координата Y центра овала.
     * @param diam1   Диаметр овала по горизонтали.
     * @param diam2   Диаметр овала по вертикали.
     */
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Рисует контур овала по указанным координатам и размерам.
     *
     * @param g       Графический контекст для отрисовки.
     * @param centerX Координата X центра овала.
     * @param centerY Координата Y центра овала.
     * @param diam1   Диаметр овала по горизонтали.
     * @param diam2   Диаметр овала по вертикали.
     */
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Рисует робота по указанным координатам и направлению.
     *
     * @param g         Графический контекст для отрисовки.
     * @param x         Координата X центра робота.
     * @param y         Координата Y центра робота.
     * @param direction Направление робота в радианах.
     */
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

    /**
     * Рисует цель по указанным координатам.
     *
     * @param g Графический контекст для отрисовки.
     * @param x Координата X центра цели.
     * @param y Координата Y центра цели.
     */
    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
