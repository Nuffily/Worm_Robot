package interfaces;

import model.FrameType;

public interface StateTrackable {
    abstract public void toDefaultState();

    abstract public FrameType getId();
}
