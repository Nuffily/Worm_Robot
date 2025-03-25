package gui;

import javax.swing.JInternalFrame;
import java.beans.PropertyVetoException;
import java.io.Serializable;


public class FrameState implements Serializable {
    private int locationX;
    private int locationY;
    public int width;
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

    public FrameState(String name) {
        switch (name) {
            case "logWindow":
                locationX = 10;
                locationY = 10;
                width = 200;
                height = 500;
                isClosed = false;
                isHidden = false;
                break;
            case "gameWindow":
                locationX = 220;
                locationY = 10;
                width = 600;
                height = 500;
                isClosed = false;
                isHidden = false;
                break;
        }
    }

    public void changeState(JInternalFrame frame) {
        try {
            frame.setLocation(locationX, locationY);
            frame.setSize(width, height);
            frame.setIcon(isHidden);
            if (IsClosed())
                frame.setClosed(isClosed);
        } catch (PropertyVetoException e) {
            System.out.println(e.getMessage());
        }
    }


    public boolean IsClosed() {
        return isClosed;
    }
}
