package utils;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Component;
import java.util.Set;
import java.util.HashSet;
import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;

public class TextAreaTab {

    public static void keyListenerMethod(JTextArea textArea) {
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    // 1. Detenemos el comportamiento por defecto (indentar)
                    e.consume();

                    // 2. Obtenemos el gestor de foco actual
                    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

                    // 3. Forzamos el salto
                    if (e.isShiftDown()) {
                        manager.focusPreviousComponent();
                    } else {
                        manager.focusNextComponent();
                    }
                }
            }
        });
    }
}
