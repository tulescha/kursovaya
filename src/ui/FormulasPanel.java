package ui;

import javax.swing.*;
import java.awt.*;

public class FormulasPanel extends JPanel {
    public FormulasPanel() {
        setLayout(new BorderLayout());
        JTextArea info = new JTextArea("Ключевые формулы, используемые в расчетах кислородной фурмы и сопла Лаваля.");
        info.setEditable(false);
        info.setFont(new Font("Monospaced", Font.BOLD, 14));
        add(new JScrollPane(info), BorderLayout.CENTER);
    }
}