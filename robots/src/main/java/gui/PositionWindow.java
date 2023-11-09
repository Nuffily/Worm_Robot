package main.java.gui;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class PositionWindow extends JInternalFrame implements Observer, Translatable {
    private final JLabel labelX;
    private final JLabel labelY;
    private final Robot m_RobotModel;


    public PositionWindow(Robot robotModel, int width, int height) {
        super("Координаты робота", true, true, true, true);
        m_RobotModel = robotModel;
        JPanel panel = new JPanel(new BorderLayout());
        m_RobotModel.addObserver(this);
        labelX = new JLabel("X: %f".formatted(m_RobotModel.getM_robotPositionX()));
        labelY = new JLabel("Y: %f".formatted(m_RobotModel.getM_robotPositionY()));
        panel.add(labelX, BorderLayout.BEFORE_FIRST_LINE);
        panel.add(labelY, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(width, height);
        setLocation(1600, 50);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals(Robot.ROBOT_POSITION_CHANGED)) EventQueue.invokeLater(this::updateCoords);
    }

    void updateCoords() {
        labelX.setText("X: %f".formatted(m_RobotModel.getM_robotPositionX()));
        labelY.setText("Y: %f".formatted(m_RobotModel.getM_robotPositionY()));
    }

    @Override
    public void translate(ResourceBundle bundle) {
        setTitle(bundle.getString("positionWindowHeader"));
    }
}
