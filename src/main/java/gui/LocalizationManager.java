package gui;

import javax.swing.JMenuItem;
import java.awt.Component;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

public class LocalizationManager {
    private Locale currentLocale = Locale.getDefault();
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", currentLocale);
    private final Map<String, Locale> supportedLocales = new LinkedHashMap<>();
    private final Map<Component, String> components = new WeakHashMap<>();


    LocalizationManager() {
        supportedLocales.put("English", Locale.of("en"));
        supportedLocales.put("Русский", Locale.of("ru"));
    }

    public void addComponent(Component component, String key) {
        components.put(component, key);
    }

    public void updateAll() {
        for (Component comp : components.keySet()) {
            if (comp instanceof JMenuItem) {
                ((JMenuItem) comp).setText(getString(components.get(comp)));
            }
        }
    }

    public void setLocale(Locale locale) {
        currentLocale = locale;
        resourceBundle = ResourceBundle.getBundle("messages", currentLocale);
    }

    public String getString(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public String getFormattedString(String key, Object... args) {
        try {
            return MessageFormat.format(resourceBundle.getString(key), args);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public Map<String, Locale> getSupportedLocales() {
        return Collections.unmodifiableMap(supportedLocales);
    }
}