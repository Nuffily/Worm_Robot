package gui;

import interfaces.localizable;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;

class MyJMenu extends JMenu implements localizable {
    private final String nameKey;
    private final String descriptionKey;
    private final LocalizationManager localizator;


    MyJMenu(String name, String description, int Key, LocalizationManager localizator) {
        super(localizator.getString(name));
        getAccessibleContext().setAccessibleDescription(localizator.getString(description));
        setMnemonic(Key);
        this.localizator = localizator;

        this.nameKey = name;
        this.descriptionKey = description;
    }

    public void addMenuButton(String nameKey, int key, ActionListener listener) {
        JMenuItem item = new JMenuItem(localizator.getString(nameKey), key);
        item.addActionListener(listener);
        add(item);
        localizator.addComponent(item, nameKey);
    }

    public void updateLocale() {
        setText(localizator.getString(nameKey));
        getAccessibleContext().setAccessibleDescription(localizator.getString(descriptionKey));
    }
}