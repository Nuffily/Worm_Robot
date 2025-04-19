package gui;

import interfaces.MyFrame;
import model.FrameType;

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

    private Map<FrameType, FrameState> states;

    final private transient String directoryName = getHome() + "/Worm_robot";
    final private transient String fileName = "/state.bin";


    private ApplicationState() {
    }

    public ApplicationState(Map<FrameType, MyFrame> frames) {


        uploadAppState(frames);

        for (FrameType type : frames.keySet()) {
            if (!states.containsKey(type) || states.get(type).IsClosed())
                frames.get(type).toDefaultState();
            else
                states.get(type).changeState(frames.get(type));
        }
    }

    public void saveAppState(Map<FrameType, MyFrame> frames) {

        states = new HashMap<>();

        for (FrameType type : frames.keySet()) {
            states.put(type, new FrameState(frames.get(type)));
        }

        File directory = new File(directoryName);

        if (!directory.exists())
            directory.mkdir();

        try (OutputStream os = new FileOutputStream(directoryName + fileName);
             ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
            oos.writeObject(this);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void uploadAppState(Map<FrameType, MyFrame> frames) {

        ApplicationState applicationState;

        try (InputStream is = new FileInputStream(directoryName + fileName);
             ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is))) {
            applicationState = (ApplicationState) ois.readObject();
            this.states = applicationState.states;
        } catch (IOException | ClassNotFoundException e) {

            states = new HashMap<>();

            for (FrameType type : frames.keySet()) {
                frames.get(type).toDefaultState();
                states.put(type, new FrameState(frames.get(type)));
            }
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
