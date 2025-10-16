package ui;

import javax.swing.*;
import java.awt.*;

public class EquipmentPanel extends JPanel {
    public EquipmentPanel() {
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                "Визуализация оборудования: Сопло Лаваля и Кислородная Фурма",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Serif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JTabbedPane equipmentTabs = new JTabbedPane();

        // Вкладка 1: Сопло Лаваля
        JLabel lavalNozzle = new JLabel("Схема Сопла Лаваля (Конфузор -> Критическое сечение -> Диффузор)", SwingConstants.CENTER);
        lavalNozzle.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        equipmentTabs.addTab("Сопло Лаваля", lavalNozzle);

        // Вкладка 2: Кислородная фурма
        JLabel oxygenLance = new JLabel("Схема Кислородной Фурмы (Подача O₂ в металлургический агрегат)", SwingConstants.CENTER);
        oxygenLance.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        equipmentTabs.addTab("Кислородная Фурма", oxygenLance);

        add(equipmentTabs, BorderLayout.CENTER);
    }
}