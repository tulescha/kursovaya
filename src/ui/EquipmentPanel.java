package ui;

import javax.swing.*;
import java.awt.*;

public class EquipmentPanel extends JPanel {
    public EquipmentPanel() {
        setLayout(new GridBagLayout());
        JLabel label = new JLabel("Описание и схемы оборудования, связанного с расчетами.", SwingConstants.CENTER);
        add(label);
    }
}