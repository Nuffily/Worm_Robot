package robots.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import robots.log.Logger;

/**
 * Что требуется сделать: 1. Метод создания меню перегружен функционалом и трудно читается. Следует
 * разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        // Make the big window be indented 50 pixels from each edge
        // of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    // protected JMenuBar createMenuBar() {
    // JMenuBar menuBar = new JMenuBar();
    //
    // //Set up the lone menu.
    // JMenu menu = new JMenu("Document");
    // menu.setMnemonic(KeyEvent.VK_D);
    // menuBar.add(menu);
    //
    // //Set up the first menu item.
    // JMenuItem menuItem = new JMenuItem("New");
    // menuItem.setMnemonic(KeyEvent.VK_N);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(
    // KeyEvent.VK_N, ActionEvent.ALT_MASK));
    // menuItem.setActionCommand("new");
    //// menuItem.addActionListener(this);
    // menu.add(menuItem);
    //
    // //Set up the second menu item.
    // menuItem = new JMenuItem("Quit");
    // menuItem.setMnemonic(KeyEvent.VK_Q);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(
    // KeyEvent.VK_Q, ActionEvent.ALT_MASK));
    // menuItem.setActionCommand("quit");
    //// menuItem.addActionListener(this);
    // menu.add(menuItem);
    //
    // return menuBar;
    // }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext()
                .setAccessibleDescription("Управление режимом отображения приложения");
        addSystemShameTo(lookAndFeelMenu);
        addUniversalSchemeTo(lookAndFeelMenu);

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");
        addLogMessageTo(testMenu);

        JMenu closeTab = new JMenu("Закрыть");
        addCloseOption(closeTab);

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(closeTab);
        return menuBar;
    }

    private void addCloseOption(JMenu closeTab) {
        closeTab.getAccessibleContext().setAccessibleDescription("Закрыть приложение");
        JMenuItem button = new JMenuItem("Выйти", KeyEvent.VK_Q);
        button.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.SHIFT_DOWN_MASK));

        button.addActionListener((event) -> {
            String[] options = {"Да", "Нет"};
            int sure = JOptionPane.showOptionDialog(this, "Вы уверены?", "Закрыть приложение? ",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
                    options[1]);
            if (JOptionPane.YES_OPTION == sure) {
                WindowEvent closeEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
            }
        });

        closeTab.add(button);
    }

    private void addSystemShameTo(JMenu lookAndFeelMenu) {
        JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);
    }

    private void addUniversalSchemeTo(JMenu lookAndFeelMenu) {
        JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
        crossplatformLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(crossplatformLookAndFeel);
    }

    private void addLogMessageTo(JMenu testMenu) {
        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug("Новая строка");
        });
        testMenu.add(addLogMessageItem);
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
