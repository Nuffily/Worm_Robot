package gui;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.JInternalFrame;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;

public class ApplicationState {
    private FrameState logWindowState;
    private FrameState gameWindowState;

    private LogWindow logWindow;
    private GameWindow gameWindow;

    public ApplicationState() {}

    public ApplicationState(GameWindow gameWindow, LogWindow logWindow) {
        this.logWindow = logWindow;
        this.gameWindow = gameWindow;
    }

    public void saveAppState() {
        this.logWindowState = new FrameState(logWindow);
        this.gameWindowState = new FrameState(gameWindow);
        try {
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new File("state.json"), this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void uploadAppState() {
        ApplicationState applicationState = new ApplicationState();
        try {
            applicationState = new ObjectMapper().readValue(new File("state.json"), ApplicationState.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        applicationState.logWindowState.changeState(logWindow);
        applicationState.gameWindowState.changeState(gameWindow);
    }

    public FrameState getGameWindowState() {
        return gameWindowState;
    }

    public FrameState getLogWindowState() {
        return logWindowState;
    }


}
