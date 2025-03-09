package gui;

import javax.swing.JInternalFrame;
import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyVetoException;


public class FrameState {
    private Point location;
    private Dimension size;
    private boolean icon;
    private boolean closed;

    public FrameState() {
    }

    public FrameState(JInternalFrame frame) {
        location = frame.getLocation();
        size = frame.getSize();
        icon = frame.isIcon();
        closed = frame.isClosed();
    }

    public void changeState(JInternalFrame frame) {
        try {
            frame.setLocation(this.getLocation());
            frame.setSize(this.getSize());
            frame.setIcon(this.isIcon());
            frame.setClosed(this.isClosed());
        } catch (PropertyVetoException e) {
            System.out.println(e.getMessage());
        }
    }

    public void toDefaultLogWindowState() {
        location = new Point(10, 10);
        size = new Dimension(200, 500);
        closed = false;
        icon = false;
    }

    public void toDefaultGameWindowState() {
        location = new Point(220, 10);
        size = new Dimension(600, 500);
        closed = false;
        icon = false;
    }

    public Dimension getSize() {
        return size;
    }

    public boolean isClosed() {
        return closed;
    }

    public Point getLocation() {
        return location;
    }

    public boolean isIcon() {
        return icon;
    }
}
