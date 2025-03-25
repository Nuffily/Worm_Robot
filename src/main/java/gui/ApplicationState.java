package gui;

import javax.swing.JInternalFrame;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ApplicationState implements Serializable {

    private Map<String, FrameState> states;

    private ApplicationState() {
    }

    public ApplicationState(Map<String, JInternalFrame> frames) {

        uploadAppState(frames);

        for (String windowName : frames.keySet()) {
            if (!states.containsKey(windowName) || states.get(windowName).IsClosed())
                new FrameState(windowName).changeState(frames.get(windowName));
            else
                states.get(windowName).changeState(frames.get(windowName));
        }
    }

    public void saveAppState(Map<String, JInternalFrame> frames) {

        states = new HashMap<>();

        for (String windowName : frames.keySet()) {
            states.put(windowName, new FrameState(frames.get(windowName)));
        }

        File directory = new File(getHome() + "/Worm_robot");

        if (!directory.exists())
            directory.mkdir();

        try (OutputStream os = new FileOutputStream(directory + "/state.bin");
             ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
            oos.writeObject(this);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void uploadAppState(Map<String, JInternalFrame> frames) {
        ApplicationState applicationState;

        try (InputStream is = new FileInputStream(getHome() + "/Worm_robot/state.bin");
             ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is))) {
            applicationState = (ApplicationState) ois.readObject();
            this.states = applicationState.states;
        } catch (IOException | ClassNotFoundException e) {

            states = new HashMap<>();

            for (String windowName : frames.keySet())
                states.put(windowName, new FrameState(windowName));
        }
    }

    private String getHome() {
        String home = System.getProperty("user.home");
        if (home == null) {
            home = System.getenv("HOME");
        }
        return home;
    }
}
