package gui;

import model.RobotState;
import model.Point;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class WormRobot {

    private double robotX;
    private double robotY;
    private double robotDirection;

    private final double maxVelocity = 0.1;
    private final double maxAngularVelocity = 0.001;
    private final double tooCloseCoefficient = 80;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public WormRobot(int x, int y) {
        robotX = x;
        robotY = y;
    }

    private double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;

        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    protected void onModelUpdateEvent(Point target) {
        double distance = distance(target.getX(), target.getY(), robotX, robotY);

        if (distance < 0.5) {
            return;
        }

        double angleToTarget = angleTo(robotX, robotY, target.getX(), target.getY());
        double angle = getAngleBetweenTwo(robotDirection, angleToTarget);

        boolean tooClose = distance < Math.sqrt(Math.abs(angle)) * tooCloseCoefficient;
        double angularVelocity = getAngularVelocity(angle, tooClose);

        moveRobot(maxVelocity, angularVelocity, 10);
    }

    private double getAngularVelocity(double angle, boolean tooClose) {
        if (Math.abs(angle) < 0.01)
            return 0;
        if ((angle > 0 && !tooClose) || (angle < 0 && tooClose))
            return maxAngularVelocity;
        else
            return -maxAngularVelocity;
    }

    private double getAngleBetweenTwo(double firstAngle, double secondAngle) {
        if (firstAngle - Math.PI > secondAngle)
            return secondAngle + 2 * Math.PI - firstAngle;
        else if (secondAngle - Math.PI > firstAngle)
            return secondAngle - firstAngle - 2 * Math.PI;
        else
            return secondAngle - firstAngle;
    }

    private double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        else return Math.min(value, max);
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

        robotDirection = asNormalizedRadians(robotDirection + angularVelocity * duration);

        notifyListeners();
    }

    private double asNormalizedRadians(double angle) {
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
