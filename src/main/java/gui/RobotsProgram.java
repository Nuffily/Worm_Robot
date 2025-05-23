package gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Font;

public class RobotsProgram {
    public static void main(String[] args) {

        try {
            configureUI();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.initialize();
        });
    }

    static private void configureUI() throws Exception {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.ITALIC, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Comic Sans MS", Font.PLAIN, 12));
    }
}
