package gui;

import interfaces.StateTrackable;
import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.nimbus.State;
import model.FrameType;

public class GameWindow extends JInternalFrame implements StateTrackable
{
    private final GameVisualizer m_visualizer;
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
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
