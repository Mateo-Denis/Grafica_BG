package utils;

import javax.swing.text.*;
import java.util.regex.Pattern;

public class NumberInputVerifier extends DocumentFilter {

    // Permite números con UN solo punto decimal
    private static final Pattern PATTERN =
            Pattern.compile("\\d*(\\.\\d*)?");

    @Override
    public void insertString(FilterBypass fb, int offset,
                             String string, AttributeSet attr)
            throws BadLocationException {

        String currentText =
                fb.getDocument().getText(0, fb.getDocument().getLength());

        String newText =
                currentText.substring(0, offset)
                        + string
                        + currentText.substring(offset);

        if (PATTERN.matcher(newText).matches()) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length,
                        String text, AttributeSet attrs)
            throws BadLocationException {

        String currentText =
                fb.getDocument().getText(0, fb.getDocument().getLength());

        String newText =
                currentText.substring(0, offset)
                        + text
                        + currentText.substring(offset + length);

        if (PATTERN.matcher(newText).matches()) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length)
            throws BadLocationException {
        super.remove(fb, offset, length);
    }
}
