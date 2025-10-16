package ui;

import javax.swing.*;
import java.awt.*;

public class AlgorithmPanel extends JPanel {
    public AlgorithmPanel() {
        setLayout(new BorderLayout());
        JTextArea info = new JTextArea("Визуализация алгоритмов для расчетов кислородной фурмы и сопла Лаваля.");
        info.setEditable(false);
        info.setFont(new Font("Monospaced", Font.BOLD, 14));
        add(new JScrollPane(info), BorderLayout.CENTER);
    }
}