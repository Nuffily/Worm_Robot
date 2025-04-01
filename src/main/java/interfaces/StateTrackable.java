package interfaces;

import javax.swing.JInternalFrame;
import model.FrameType;

abstract public class StateTrackable extends JInternalFrame {
    abstract public void toDefaultState();

    abstract public FrameType getId();
}
