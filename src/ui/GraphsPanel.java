package ui;

import javax.swing.*;
import java.awt.*;

public class GraphsPanel extends JPanel {
    public GraphsPanel() {
        setLayout(new GridBagLayout());
        JLabel label = new JLabel("Здесь будут отображаться графики результатов расчетов (например, JFreeChart).", SwingConstants.CENTER);
        add(label);
    }
}