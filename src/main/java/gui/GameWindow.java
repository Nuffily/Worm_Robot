package gui;

import interfaces.MyFrame;
import model.FrameType;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class GameWindow extends MyFrame {
    private final GameVisualizer m_visualizer;
    private final LocalizationManager localizator;

    public GameWindow(LocalizationManager localizator, WormRobot robot) {
        super(localizator.getString("window.game"), true, true, true, true);
        m_visualizer = new GameVisualizer(robot);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        this.localizator = localizator;
        pack();
    }

    public void updateLocale() {
        setTitle(localizator.getString("window.game"));
    }

    public void toDefaultState() {
        setLocation(220, 10);
        setSize(600, 500);
        isIcon = false;
    }

    public FrameType getId() {
        return FrameType.GAME_WINDOW;
    }
}
