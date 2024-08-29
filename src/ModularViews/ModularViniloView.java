import javax.swing.*;
import java.awt.*;

public class ModularViniloView extends JPanel {
    public ModularViniloView() {
        setLayout(new GridLayout(2, 1));
        JTextField field0 = new JTextField();
        add(field0);
        JComboBox field1 = new JComboBox();
        add(field1);
    }
}
