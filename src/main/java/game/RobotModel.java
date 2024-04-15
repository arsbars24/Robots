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
    private double velocity = 0.1; // Пример значения скорости
    private double angularVelocity = 0.001; // Пример значения угловой скорости
    private double duration = 1.0; // Фиксированная длительность движения
    private static final double maxVelocity = 0.4;
    private static final double maxAngularVelocity = 0.01;

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
     */
    public void moveRobot() {
        // Рассчитываем расстояние до цели
        double distance = distance(targetPositionX, targetPositionY, positionX, positionY);

        // Если робот уже достиг цели, прекращаем движение
        if (distance < 0.5) {
            return;
        }

        // Рассчитываем угол поворота к цели
        double angleToTarget = angleTo(positionX, positionY, targetPositionX, targetPositionY);
        double angleDifference = angleToTarget - direction;

        // Определяем направление поворота
        double angularVelocity = 0;
        if (angleDifference > Math.PI) {
            angleDifference -= 2 * Math.PI; // Если разница больше 180 градусов, то берем противоположное направление
        } else if (angleDifference < -Math.PI) {
            angleDifference += 2 * Math.PI; // Если разница меньше -180 градусов, то берем противоположное направление
        }

        // Определяем направление поворота
        if (angleDifference > 0) {
            angularVelocity = maxAngularVelocity;
        } else if (angleDifference < 0) {
            angularVelocity = -maxAngularVelocity;
        }

        // Рассчитываем скорость на основе расстояния до цели
        double velocity = Math.min(maxVelocity, distance);

        // Фиксированная длительность движения
        double duration = 1.0;

        // Обновляем позицию и направление робота
        double newX = positionX + velocity * Math.cos(direction) * duration;
        double newY = positionY + velocity * Math.sin(direction) * duration;
        double newDirection = asNormalizedRadians(direction + angularVelocity * duration);

        updatePosition(newX, newY, newDirection);
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

    protected boolean isAtTarget() {
        return Math.round(positionX) == targetPositionX
                && Math.round(positionY) == targetPositionY;
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    /**
     * Рассчитывает угол между двумя точками.
     */
    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }
    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }
}
