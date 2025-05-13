package model;

public class RobotState {
    private final double x;
    private final double y;
    private final double direction;

    public RobotState(double x, double y, double direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDirection() {
        return direction;
    }
}
