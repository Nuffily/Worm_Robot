package gui;

import interfaces.MyFrame;
import model.FrameType;
import model.RobotState;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class RobotInfoWindow extends MyFrame {

    private final LocalizationManager localizator;

    private final JLabel xLabel;
    private final JLabel yLabel;
    private final JLabel infoLabel;

    public RobotInfoWindow(LocalizationManager localizator, WormRobot robot) {
        super(localizator.getString("window.robot_info"), true, true, true, true);
        setLayout(new java.awt.GridLayout(4, 1));

        infoLabel = new JLabel(localizator.getString("window.coordinates"), SwingConstants.CENTER);

        xLabel = new JLabel("X: " + 100, SwingConstants.CENTER);
        yLabel = new JLabel("Y: " + 100, SwingConstants.CENTER);

        add(infoLabel);
        add(xLabel);
        add(yLabel);

        robot.addPropertyChangeListener(evt -> {
            RobotState robotState = (RobotState) evt.getNewValue();

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
