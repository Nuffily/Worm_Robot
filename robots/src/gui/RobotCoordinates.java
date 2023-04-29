package gui;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class RobotCoordinates extends JInternalFrame implements Observer
{
    private final TextArea coordinatesArea;

    public RobotCoordinates()
    {
        super("Координаты робота", true, true, true, true);

        coordinatesArea = new TextArea();
        coordinatesArea.setSize(100, 50);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(coordinatesArea, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    private void updateDisplayedRobotCoordinates(Pair<Double, Double> newCoordinates)
    {
        coordinatesArea.setText("X: " + newCoordinates.getKey() + "\nY: " + newCoordinates.getValue());
        coordinatesArea.invalidate();
    }

    @Override
    public void update(Observable observable, Object o)
    {
        updateDisplayedRobotCoordinates((Pair<Double, Double>) o);
    }
}