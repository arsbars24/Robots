package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

/**
 * Панель для визуализации игровых объектов.
 */
public class GameVisualizer extends JPanel
{
    private final Timer m_timer = initTimer();

    /**
     * Инициализирует и возвращает таймер с указанным именем и настройкой daemon.
     *
     * @return Новый таймер.
     */
    private static Timer initTimer() 
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100; 
    private volatile double m_robotDirection = 0; 

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;
    
    private static final double maxVelocity = 0.1; 
    private static final double maxAngularVelocity = 0.001;

    /**
     * Конструктор класса GameVisualizer.
     * Инициализирует таймеры, устанавливает слушателя мыши и включает двойную буферизацию.
     */
    public GameVisualizer() 
    {
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onModelUpdateEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    /**
     * Устанавливает позицию цели.
     *
     * @param p Точка, в которой устанавливается цель.
     */
    protected void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    /**
     * Обработчик события перерисовки.
     */
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    /**
     * Рассчитывает расстояние между двумя точками.
     */
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

    /**
     * Расчет и обновление модели игрового объекта.
     */
    protected void onModelUpdateEvent()
    {
        double distance = distance(m_targetPositionX, m_targetPositionY, 
            m_robotPositionX, m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }
        
        moveRobot(velocity, angularVelocity, 10);
    }

    /**
     * Применяет ограничения к значению в пределах указанных минимума и максимума.
     *
     * @param value Значение, которое нужно ограничить.
     * @param min   Минимальное значение.
     * @param max   Максимальное значение.
     * @return Ограниченное значение.
     */
    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    /**
     * Перемещает робота.
     */
    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity * 
            (Math.sin(m_robotDirection  + angularVelocity * duration) -
                Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity * 
            (Math.cos(m_robotDirection  + angularVelocity * duration) -
                Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration); 
        m_robotDirection = newDirection;
    }

    /**
     * Приводит угол к нормализованному виду в радианах.
     */
    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    /**
     * Округляет число.
     */
    private static int round(double value)
    {
        return (int)(value + 0.5);
    }

    /**
     * Переопределенный метод отрисовки компонента.
     *
     * @param g Графический контекст для отрисовки.
     */
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g; 
        drawRobot(g2d, round(m_robotPositionX), round(m_robotPositionY), m_robotDirection);
        drawTarget(g2d, m_targetPositionX, m_targetPositionY);
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
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
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
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
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
    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        int robotCenterX = round(m_robotPositionX); 
        int robotCenterY = round(m_robotPositionY);
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY); 
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }

    /**
     * Рисует цель по указанным координатам.
     *
     * @param g Графический контекст для отрисовки.
     * @param x Координата X центра цели.
     * @param y Координата Y центра цели.
     */
    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
