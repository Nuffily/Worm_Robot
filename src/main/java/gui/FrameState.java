package gui;

import javax.swing.JInternalFrame;
import java.beans.PropertyVetoException;


public class FrameState {
    private int locationX;
    private int locationY;
    private int width;
    private int height;
    private boolean isHidden;
    private boolean isClosed;

    public FrameState() {
    }

    public FrameState(JInternalFrame frame) {
        locationX = frame.getLocation().x;
        locationY = frame.getLocation().y;
        width = frame.getSize().width;
        height = frame.getSize().height;
        isHidden = frame.isIcon();
        isClosed = frame.isClosed();
    }

    public void changeState(JInternalFrame frame) {
        try {
            frame.setLocation(locationX, locationY);
            frame.setSize(width, height);
            frame.setIcon(isHidden);
            frame.setClosed(isClosed);
        } catch (PropertyVetoException e) {
            System.out.println(e.getMessage());
        }
    }

    public void toDefaultLogWindowState() {
        locationX = 10;
        locationY = 10;
        width = 200;
        height = 500;
        isClosed = false;
        isHidden = false;
    }

    public void toDefaultGameWindowState() {
        locationX = 220;
        locationY = 10;
        width = 600;
        height = 500;
        isClosed = false;
        isHidden = false;
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getIsClosed() {
        return isClosed;
    }

    public boolean getIsHidden() {
        return isHidden;
    }
}
