package gui;

import interfaces.localeUpdatable;

import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

class MyJMenu extends JMenu implements localeUpdatable {
    private final String nameKey;
    private final String descriptionKey;


    MyJMenu(String name, String description, int Key) {
        super(LocalizationManager.getString(name));
        getAccessibleContext().setAccessibleDescription(LocalizationManager.getString(description));
        setMnemonic(Key);

        this.nameKey = name;
        this.descriptionKey = description;
    }

    public void addMenuButton(String nameKey, int key, ActionListener listener) {
        JMenuItem item = new JMenuItem(LocalizationManager.getString(nameKey), key);
        item.addActionListener(listener);
        add(item);
        LocalizationManager.addComponent(item, nameKey);
    }

    public void updateLocale() {
        setText(LocalizationManager.getString(nameKey));
        getAccessibleContext().setAccessibleDescription(LocalizationManager.getString(descriptionKey));
    }
}