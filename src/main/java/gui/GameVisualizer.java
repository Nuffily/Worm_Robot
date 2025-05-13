package gui;

import model.RobotState;
import model.RobotTarget;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel {
    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

//    private volatile double robotPositionX = 100;
//    private volatile double robotPositionY = 100;
//    private volatile double robotDirection = 0;

    private volatile RobotState robotState;

    private final RobotTarget target;
    private final WormRobot robot;

//    private static final double maxVelocity = 0.1;
//    private static final double maxAngularVelocity = 0.001;

    public GameVisualizer(WormRobot robot, RobotTarget target) {

        this.robot = robot;
        this.target = target;

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);

        robot.addPropertyChangeListener(evt -> {
            robotState = (RobotState)evt.getNewValue();

            repaint();
        });

//        m_timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                onModelUpdateEvent();
//            }
//        }, 0, 10);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    protected void setTargetPosition(Point p) {
        target.set(p.x, p.y);
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

//    private static double distance(double x1, double y1, double x2, double y2) {
//        double diffX = x1 - x2;
//        double diffY = y1 - y2;
//        return Math.sqrt(diffX * diffX + diffY * diffY);
//    }

//    private static double angleTo(double fromX, double fromY, double toX, double toY) {
//        double diffX = toX - fromX;
//        double diffY = toY - fromY;
//
//        return asNormalizedRadians(Math.atan2(diffY, diffX));
//    }

//    protected void onModelUpdateEvent() {
//        double distance = distance(target.getX(), target.getY(),
//                robotPositionX, robotPositionY);
//        if (distance < 0.5) {
//            return;
//        }
//        double velocity = maxVelocity;
//        double angleToTarget = angleTo(robotPositionX, robotPositionY, target.getX(), target.getY());
//        double angularVelocity = 0;
//
//        double angle;
//
//        if (robotDirection - Math.PI > angleToTarget)
//            angle = angleToTarget + 2 * Math.PI - robotDirection;
//        else if (angleToTarget - Math.PI > robotDirection)
//            angle = angleToTarget - robotDirection - 2 * Math.PI;
//        else
//            angle = angleToTarget - robotDirection;
//
//        boolean tooClose = distance < Math.sqrt(Math.abs(angle)) * 50;
//
//        System.out.println(angle);
//
//        if (Math.abs(angle) < 0.00001)
//            angularVelocity = 0;
//        if ((angle > 0 && !tooClose) || (angle < 0 && tooClose))
//            angularVelocity = maxAngularVelocity;
//        else
//            angularVelocity = -maxAngularVelocity;
//
//        moveRobot(velocity, angularVelocity, 10);
//    }

//    private static double applyLimits(double value, double min, double max) {
//        if (value < min)
//            return min;
//        else if (value > max)
//            return max;
//        else return value;
//    }

//    private void moveRobot(double velocity, double angularVelocity, double duration) {
//        velocity = applyLimits(velocity, 0, maxVelocity);
//        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
//        double newX = robotPositionX + velocity / angularVelocity *
//                (Math.sin(robotDirection + angularVelocity * duration) -
//                        Math.sin(robotDirection));
//        if (!Double.isFinite(newX)) {
//            newX = robotPositionX + velocity * duration * Math.cos(robotDirection);
//        }
//        double newY = robotPositionY - velocity / angularVelocity *
//                (Math.cos(robotDirection + angularVelocity * duration) -
//                        Math.cos(robotDirection));
//        if (!Double.isFinite(newY)) {
//            newY = robotPositionY + velocity * duration * Math.sin(robotDirection);
//        }
//        robotPositionX = newX;
//        robotPositionY = newY;
//        double newDirection = asNormalizedRadians(robotDirection + angularVelocity * duration);
//        robotDirection = newDirection;
//    }

//    private static double asNormalizedRadians(double angle) {
//        while (angle < 0) {
//            angle += 2 * Math.PI;
//        }
//        while (angle >= 2 * Math.PI) {
//            angle -= 2 * Math.PI;
//        }
//        return angle;
//    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, round(robotState.getX()), round(robotState.getY()), robotState.getDirection());
        drawTarget(g2d, target.getX(), target.getY());
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        int robotCenterX = round(robotState.getX());
        int robotCenterY = round(robotState.getY());
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
