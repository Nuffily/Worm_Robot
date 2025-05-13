package gui;

import interfaces.MyFrame;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import log.Logger;
import model.FrameType;
import model.RobotState;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TextArea;

public class RobotInfoWindow extends MyFrame {

    private final LocalizationManager localizator;
    private final WormRobot robot;
    private RobotState robotState;

    private JLabel xLabel;
    private JLabel yLabel;
    private JLabel infoLabel;

    public RobotInfoWindow(LocalizationManager localizator, WormRobot robot) {
        super(localizator.getString("window.robot_info"), true, true, true, true);
        setLayout(new java.awt.GridLayout(4, 1));
        this.robot = robot;

        infoLabel = new JLabel(localizator.getString("window.coordinates"), SwingConstants.CENTER);

        xLabel = new JLabel("X: " + 100, SwingConstants.CENTER);
        yLabel = new JLabel("Y: " + 100, SwingConstants.CENTER);

        add(xLabel);
        add(yLabel);

        robot.addPropertyChangeListener(evt -> {
            robotState = (RobotState)evt.getNewValue();

            xLabel.setText("X: " + round(robotState.getX()));
            yLabel.setText("Y: " + round(robotState.getY()));
        });

        this.localizator = localizator;

        setMinimumSize(this.getSize());
        pack();
    }

    public void updateLocale() {
        setTitle(localizator.getString("window.robot_info"));
        infoLabel.setText(localizator.getString("window.coordinates"));
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    public void toDefaultState() {
        setLocation(10, 520);
        setSize(200, 150);
        isIcon = false;
    }

    public FrameType getId() {
        return FrameType.ROBOT_INFO_MENU;
    }
}
