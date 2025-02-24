package gui;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {
    public static void main(String[] args) {

//        UIManager.getLookAndFeelDefaults().keySet().forEach(System.out::println);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("OptionPane.yesButtonText", "Да");
            UIManager.put("OptionPane.noButtonText", "Нет");
            UIManager.put("OptionPane.okButtonText", "Понятно");
            UIManager.put("OptionPane.Message", "Понятно");
            UIManager.put("OptionPane.messageDialogTitle", "Сообщение");
            UIManager.put("OptionPane.confirmDialogTitle", "Подтверждение");
            UIManager.put("OptionPane.messageFont", new Font("Arial", Font.ITALIC, 14));
            UIManager.put("OptionPane.buttonFont", new Font("Comic Sans MS", Font.PLAIN, 12));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    frame.closeApprove();
                }
            });
        });
    }
}
