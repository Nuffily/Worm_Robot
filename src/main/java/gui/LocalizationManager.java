package gui;

import interfaces.MyFrame;

import javax.swing.JMenuItem;
import java.awt.Component;
import java.util.*;
import java.text.MessageFormat;

public class LocalizationManager {
    private static Locale currentLocale = Locale.getDefault();
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", currentLocale);
    private static final Map<String, Locale> supportedLocales = new LinkedHashMap<>();
    private static final Map<Component, String> components = new WeakHashMap<>();

    static {
        supportedLocales.put("English", Locale.of("en"));
        supportedLocales.put("Русский", Locale.of("ru"));
    }

    public static void addComponent(Component component, String key) {
        components.put(component, key);
    }

    public static void updateAll() {
        for ( Component comp: components.keySet()) {
            if (comp instanceof JMenuItem) {
                ((JMenuItem) comp).setText(getString(components.get(comp)));
            }
        }
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        resourceBundle = ResourceBundle.getBundle("messages", currentLocale);
    }

    public static String getString(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static String getFormattedString(String key, Object... args) {
        try {
            return MessageFormat.format(resourceBundle.getString(key), args);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static Map<String, Locale> getSupportedLocales() {
        return Collections.unmodifiableMap(supportedLocales);
    }
}