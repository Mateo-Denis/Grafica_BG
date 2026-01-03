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
    public static void changeTabKeyBehavior(JTextArea textArea) {
        // 1. Desvinculamos la tecla TAB de la acción de insertar tabulación
        textArea.getInputMap().put(KeyStroke.getKeyStroke("TAB"), "none");
        textArea.getInputMap().put(KeyStroke.getKeyStroke("shift TAB"), "none");

        // 2. Definimos explícitamente el comportamiento de navegación
        Set<AWTKeyStroke> forwardKeys = new HashSet<>();
        forwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
        textArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);

        Set<AWTKeyStroke> backwardKeys = new HashSet<>();
        backwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK));
        textArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);
    }

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
