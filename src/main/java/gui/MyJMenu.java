package gui;

import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

class MyJMenu extends JMenu {

    MyJMenu(String name, String Description, int Key) {
        super(name);
        setMnemonic(Key);
        getAccessibleContext().setAccessibleDescription(Description);
    }

    public void addMenuButton(String name, int key, ActionListener listener) {
        JMenuItem item = new JMenuItem(name, key);
        item.addActionListener(listener);
        add(item);
    }
}