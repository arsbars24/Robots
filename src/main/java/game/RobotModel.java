package game;

import java.util.Observable;

/**
 * Модель робота.
 */
public class RobotModel extends Observable {
    private double positionX;
    private double positionY;
    private double direction;
    private int targetPositionX;
    private int targetPositionY;

    public RobotModel(double initialPositionX, double initialPositionY) {
        this.positionX = initialPositionX;
        this.positionY = initialPositionY;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getDirection() {
        return direction;
    }

    public int getTargetPositionX() {
        return targetPositionX;
    }

    public int getTargetPositionY() {
        return targetPositionY;
    }

    /**
     * Устанавливает позицию цели.
     *
     * @param x Координата X цели.
     * @param y Координата Y цели.
     */
    public void setTargetPosition(int x, int y) {
        this.targetPositionX = x;
        this.targetPositionY = y;
    }

    /**
     * Обновляет позицию и направление робота.
     *
     * @param newX      Новая координата X робота.
     * @param newY      Новая координата Y робота.
     * @param newDirect Новое направление робота.
     */
    public void updatePosition(double newX, double newY, double newDirect) {
        this.positionX = newX;
        this.positionY = newY;
        this.direction = newDirect;

        setChanged();
        notifyObservers();
    }

    /**
     * Перемещает робота.
     *
     * @param velocity          Скорость перемещения робота.
     * @param angularVelocity   Угловая скорость робота.
     * @param duration          Длительность перемещения.
     */
    public void moveRobot(double velocity, double angularVelocity, double duration) {
        double newX = getPositionX() + velocity / angularVelocity *
                (Math.sin(getDirection() + angularVelocity * duration) -
                        Math.sin(getDirection()));
        if (!Double.isFinite(newX)) {
            newX = getPositionX() + velocity * duration * Math.cos(getDirection());
        }
        double newY = getPositionY() - velocity / angularVelocity *
                (Math.cos(getDirection() + angularVelocity * duration) -
                        Math.cos(getDirection()));
        if (!Double.isFinite(newY)) {
            newY = getPositionY() + velocity * duration * Math.sin(getDirection());
        }
        double newDirection = asNormalizedRadians(getDirection() + angularVelocity * duration);
        updatePosition(newX, newY, newDirection);
        setDirection(newDirection); // Установка нового направления
    }
    //FIXME изменить логику движения, чтобы робот не зацикливался в круг

    /**
     * Устанавливает новое направление робота.
     *
     * @param newDirection Новое направление робота в радианах.
     */
    public void setDirection(double newDirection) {
        this.direction = asNormalizedRadians(newDirection);
        setChanged();
        notifyObservers();
    }

    /**
     * Приводит угол к нормализованному виду в радианах.
     */
    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }
}
