package interfaces;

import javax.swing.JInternalFrame;

abstract public class MyFrame extends JInternalFrame implements StateTrackable, localeUpdatable {

    public MyFrame(String title, boolean resizable, boolean closable,
                   boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

    abstract public void updateLocale();

}
