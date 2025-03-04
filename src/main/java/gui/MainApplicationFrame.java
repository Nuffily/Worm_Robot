package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import log.Logger;


import java.awt.event.KeyEvent;

import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {

    private class myJMenu extends JMenu {

        myJMenu(String name, String Description, int Key) {
            super(name);
            setMnemonic(Key);
            getAccessibleContext().setAccessibleDescription(Description);
        }

        public void addMenuButton(String name, int key, java.awt.event.ActionListener listener) {
            JMenuItem item = new JMenuItem(name, key);
            item.addActionListener(listener);
            add(item);
        }

    }

    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {


        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    protected void closeApprove() {
        int ans = showConfirmDialog(this, "Вы правда хотите выйти?",
                "Подтвердите выход", YES_NO_OPTION);

        if (ans == 0) System.exit(0);
        else showMessageDialog(this, "Правильно, оставайся");
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

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        myJMenu lookAndFeelMenu = new myJMenu("Режим отображения",
                "Управление режимом отображения приложения", KeyEvent.VK_V);

        lookAndFeelMenu.addMenuButton("Системная схема", KeyEvent.VK_S,
                (_) -> setLookAndFeel(UIManager.getSystemLookAndFeelClassName()));
        lookAndFeelMenu.addMenuButton("Универсальная схема", KeyEvent.VK_S,
                (_) -> setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()));
        lookAndFeelMenu.addMenuButton("Базовая схема", KeyEvent.VK_S,
                (_) -> setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"));

        menuBar.add(lookAndFeelMenu);

        myJMenu testMenu = new myJMenu("Тесты", "Тестовые команды", KeyEvent.VK_T);

        testMenu.addMenuButton("Сообщение в лог", KeyEvent.VK_S,
                (_) -> Logger.debug("Новая строка"));

        menuBar.add(testMenu);

        myJMenu fileMenu = new myJMenu("Файл", "Программа", KeyEvent.VK_Q);

        fileMenu.addMenuButton("Выход", KeyEvent.VK_S,
                (_) -> closeApprove());

        menuBar.add(fileMenu);

        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
