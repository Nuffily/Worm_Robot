package gui;

import model.RobotState;
import model.RobotTarget;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class WormRobot {

    private final RobotTarget target;
    private final Timer timer;

    private double robotX;
    private double robotY;
    private double robotDirection;

    private final double maxVelocity = 0.1;
    private final double maxAngularVelocity = 0.001;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public WormRobot(RobotTarget target, int x, int y) {
        this.target = target;
        robotX = x;
        robotY = y;

        timer = new Timer("events generator", true);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 10);

    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    protected void onModelUpdateEvent() {
        double distance = distance(target.getX(), target.getY(),
                robotX, robotY);
        if (distance < 0.5) {
            return;
        }

        double angleToTarget = angleTo(robotX, robotY, target.getX(), target.getY());
        double angularVelocity;

        double angle;

        if (robotDirection - Math.PI > angleToTarget)
            angle = angleToTarget + 2 * Math.PI - robotDirection;
        else if (angleToTarget - Math.PI > robotDirection)
            angle = angleToTarget - robotDirection - 2 * Math.PI;
        else
            angle = angleToTarget - robotDirection;

        boolean tooClose = distance < Math.sqrt(Math.abs(angle)) * 70;

        System.out.println(angle);

        if (Math.abs(angle) < 0.00001)
            angularVelocity = 0;
        if ((angle > 0 && !tooClose) || (angle < 0 && tooClose))
            angularVelocity = maxAngularVelocity;
        else
            angularVelocity = -maxAngularVelocity;

        moveRobot(maxVelocity, angularVelocity, 10);
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        else if (value > max)
            return max;
        else return value;
    }

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = robotX + velocity / angularVelocity *
                (Math.sin(robotDirection + angularVelocity * duration) -
                        Math.sin(robotDirection));
        if (!Double.isFinite(newX)) {
            newX = robotX + velocity * duration * Math.cos(robotDirection);
        }
        double newY = robotY - velocity / angularVelocity *
                (Math.cos(robotDirection + angularVelocity * duration) -
                        Math.cos(robotDirection));
        if (!Double.isFinite(newY)) {
            newY = robotY + velocity * duration * Math.sin(robotDirection);
        }
        robotX = newX;
        robotY = newY;
        double newDirection = asNormalizedRadians(robotDirection + angularVelocity * duration);
        robotDirection = newDirection;

        notifyListeners();
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    private void notifyListeners() {
        support.firePropertyChange("position", null,
                new RobotState(robotX, robotY, robotDirection));
    }
}
