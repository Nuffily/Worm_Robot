package gui;

import interfaces.StateTrackable;
import log.Logger;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class MainApplicationFrame extends JFrame {

    private final JDesktopPane desktopPane = new JDesktopPane();
    private final ApplicationState state;
    private final Map<String, JInternalFrame> windows = new HashMap<>();


    public MainApplicationFrame() {

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset,
                screenSize.height - inset);

        setContentPane(desktopPane);


        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());

        state = new ApplicationState(windows);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public void initialize() {
        pack();
        setVisible(true);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApprove();
            }
        });
    }

    private void closeApprove() {
        if (shouldExit()) {
            state.saveAppState(windows);
            System.exit(0);
        } else showMessageDialog(this, "Правильно, оставайся");
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
        if (frame instanceof StateTrackable trackableFrame)
            windows.put(trackableFrame.getId(), trackableFrame);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createFileMenu());

        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            showMessageDialog(this, "Схема не меняется...");
            Logger.debug(e.getMessage());
        }
    }

    private JMenu createLookAndFeelMenu() {
        MyJMenu lookAndFeelMenu = new MyJMenu("Режим отображения",
                "Управление режимом отображения приложения", KeyEvent.VK_V);

        lookAndFeelMenu.addMenuButton("Системная схема", KeyEvent.VK_S,
                (_) -> setLookAndFeel(UIManager.getSystemLookAndFeelClassName()));
        lookAndFeelMenu.addMenuButton("Универсальная схема", KeyEvent.VK_S,
                (_) -> setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()));
        lookAndFeelMenu.addMenuButton("Базовая схема", KeyEvent.VK_S,
                (_) -> setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"));

        return lookAndFeelMenu;
    }

    private JMenu createTestMenu() {
        MyJMenu testMenu = new MyJMenu("Тесты", "Тестовые команды", KeyEvent.VK_T);

        testMenu.addMenuButton("Сообщение в лог", KeyEvent.VK_S,
                (_) -> Logger.debug("Новая строка"));

        return testMenu;
    }

    private JMenu createFileMenu() {
        MyJMenu fileMenu = new MyJMenu("Файл", "Программа", KeyEvent.VK_Q);

        fileMenu.addMenuButton("Выход", KeyEvent.VK_S,
                (_) -> closeApprove());

        return fileMenu;
    }

    private Boolean shouldExit() {
        return showConfirmDialog(this, "Вы правда хотите выйти?",
                "Подтвердите выход", YES_NO_OPTION) == YES_NO_OPTION;
    }
}
