package gui;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ApplicationState {
    private FrameState logWindowState = new FrameState();;
    private FrameState gameWindowState = new FrameState();;

    private LogWindow logWindow;
    private GameWindow gameWindow;

    public ApplicationState() {
    }

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
        } catch (Exception e) {
            applicationState.logWindowState.toDefaultLogWindowState();
            applicationState.gameWindowState.toDefaultGameWindowState();
            return;
        }

        if (applicationState.logWindowState.isClosed())
            applicationState.logWindowState.toDefaultLogWindowState();

        if (applicationState.gameWindowState.isClosed())
            applicationState.gameWindowState.toDefaultGameWindowState();

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
