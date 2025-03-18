package gui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class ApplicationState implements Serializable {
    private FrameState logWindowState = new FrameState();
    private FrameState gameWindowState = new FrameState();

    private transient LogWindow logWindow;
    private transient GameWindow gameWindow;

    private ApplicationState() {
    }

    public ApplicationState(GameWindow gameWindow, LogWindow logWindow) {
        this.logWindow = logWindow;
        this.gameWindow = gameWindow;
    }

    public void saveAppState() {
        this.logWindowState = new FrameState(logWindow);
        this.gameWindowState = new FrameState(gameWindow);

        try (OutputStream os = new FileOutputStream(getHome() + "/state.bin");
             ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
            oos.writeObject(this);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void uploadAppState() {
        ApplicationState applicationState = new ApplicationState();

        try (InputStream is = new FileInputStream(getHome() + "/state.bin");
             ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is))) {
            applicationState = (ApplicationState)  ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            applicationState.logWindowState.toDefaultLogWindowState();
            applicationState.gameWindowState.toDefaultGameWindowState();
        }

        if (applicationState.logWindowState.IsClosed())
            applicationState.logWindowState.toDefaultLogWindowState();

        if (applicationState.gameWindowState.IsClosed())
            applicationState.gameWindowState.toDefaultGameWindowState();

        applicationState.logWindowState.changeState(logWindow);
        applicationState.gameWindowState.changeState(gameWindow);
    }

    private String getHome() {
        String home = System.getProperty("user.home");
        if (home == null) {
            home = System.getenv("HOME");
        }
        return home;
    }
}
