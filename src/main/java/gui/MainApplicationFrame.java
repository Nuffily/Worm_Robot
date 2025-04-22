package gui;

import interfaces.MyFrame;
import interfaces.localeUpdatable;
import log.Logger;
import model.FrameType;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class MainApplicationFrame extends JFrame {

    private final JDesktopPane desktopPane = new JDesktopPane();
    private final ApplicationState state;
    private final Map<FrameType, MyFrame> windows = new HashMap<>();
    private final ArrayList<localeUpdatable> toUpdate = new ArrayList<>();

    public MainApplicationFrame() {
        configureUI();

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
        } else showMessageDialog(this, LocalizationManager.getString("app.stay_message"));
    }

    protected void addWindow(MyFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
        windows.put(frame.getId(), frame);
        toUpdate.add(frame);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createFileMenu());
        menuBar.add(createLanguageMenu());

        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            showMessageDialog(this,  LocalizationManager.getString("app.scheme_error"));
            Logger.debug(e.getMessage());
        }
    }

    private JMenu createLookAndFeelMenu() {
        MyJMenu lookAndFeelMenu = new MyJMenu("menu.scheme",
                "menu.scheme.tooltip", KeyEvent.VK_V);

        lookAndFeelMenu.addMenuButton("menu.scheme.system", KeyEvent.VK_S,
                (_) -> setLookAndFeel(UIManager.getSystemLookAndFeelClassName()));
        lookAndFeelMenu.addMenuButton("menu.scheme.universal", KeyEvent.VK_S,
                (_) -> setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()));
        lookAndFeelMenu.addMenuButton("menu.scheme.basic", KeyEvent.VK_S,
                (_) -> setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"));

        toUpdate.add(lookAndFeelMenu);
        return lookAndFeelMenu;
    }

    private JMenu createTestMenu() {
        MyJMenu testMenu = new MyJMenu("menu.tests", "menu.tests.tooltip", KeyEvent.VK_T);

        testMenu.addMenuButton("menu.tests.log_message", KeyEvent.VK_S,
                (_) -> Logger.debug(LocalizationManager.getString("log.new_entry")));
        testMenu.addMenuButton("menu.tests.log_messages", KeyEvent.VK_3,
                (_) -> {for (int i = 0; i < 100; i++) Logger.debug(LocalizationManager.getFormattedString("log.multiple_entries", i));});

        toUpdate.add(testMenu);
        return testMenu;
    }

    private JMenu createFileMenu() {
        MyJMenu fileMenu = new MyJMenu("menu.file", "menu.file.tooltip", KeyEvent.VK_Q);

        fileMenu.addMenuButton("menu.file.exit", KeyEvent.VK_S,
                (_) -> closeApprove());

        toUpdate.add(fileMenu);
        return fileMenu;
    }

    private JMenu createLanguageMenu() {
        MyJMenu languageMenu = new MyJMenu("menu.language", "menu.language.tooltip", KeyEvent.VK_L);

        LocalizationManager.getSupportedLocales().forEach((name, locale) -> {
            JMenuItem item = new JMenuItem(name);

            item.addActionListener(e -> {
                change(locale);});

            languageMenu.add(item);
        });

        toUpdate.add(languageMenu);
        return languageMenu;
    }

    private void change(Locale locale) {
        LocalizationManager.setLocale(locale);
        setTitle(LocalizationManager.getString("app.title"));

        JMenuBar bar = getJMenuBar();
        LocalizationManager.updateAll();
        configureUI();

        for (localeUpdatable compomemt: toUpdate)
            compomemt.updateLocale();

        SwingUtilities.updateComponentTreeUI(this);
    }

    private void configureUI() {
        setTitle(LocalizationManager.getString("app.title"));
        UIManager.put("OptionPane.yesButtonText", LocalizationManager.getString("option.yes"));
        UIManager.put("OptionPane.noButtonText", LocalizationManager.getString("option.no"));
        UIManager.put("OptionPane.okButtonText", LocalizationManager.getString("option.ok"));
        UIManager.put("OptionPane.messageDialogTitle", LocalizationManager.getString("option.message"));
        UIManager.put("OptionPane.confirmDialogTitle", LocalizationManager.getString("option.confirm"));
    }

    private Boolean shouldExit() {
        return showConfirmDialog(this, LocalizationManager.getString("app.exit_confirm"),
                LocalizationManager.getString("app.exit_title"), YES_NO_OPTION) == YES_NO_OPTION;
    }
}
