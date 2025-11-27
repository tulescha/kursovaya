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

        // Вкладка 1: Сопло Лаваля с изображением
        JPanel lavalPanel = createImagePanel("laval.jpg", "Сопло Лаваля");
        equipmentTabs.addTab("Сопло Лаваля", lavalPanel);

        // Вкладка 2: Кислородная фурма с изображением
        JPanel oxygenPanel = createImagePanel("kisl.jpg", "Кислородная Фурма");
        equipmentTabs.addTab("Кислородная Фурма", oxygenPanel);

        add(equipmentTabs, BorderLayout.CENTER);
    }

    private JPanel createImagePanel(String imagePath, String description) {
        JPanel panel = new JPanel(new BorderLayout());

        try {
            // Загрузка изображения
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image originalImage = originalIcon.getImage();

            // Масштабирование изображения для лучшего отображения
            Image scaledImage = originalImage.getScaledInstance(600, 400, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel(scaledIcon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Описание под изображением
            JLabel descLabel = new JLabel(description, SwingConstants.CENTER);
            descLabel.setFont(new Font("Serif", Font.BOLD, 16));
            descLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

            panel.add(imageLabel, BorderLayout.CENTER);
            panel.add(descLabel, BorderLayout.SOUTH);

        } catch (Exception e) {
            // Если изображение не найдено, показываем заглушку
            JLabel errorLabel = new JLabel(
                    "<html><center>Изображение не найдено: " + imagePath +
                            "<br>Разместите файл в корне проекта</center></html>",
                    SwingConstants.CENTER
            );
            errorLabel.setForeground(Color.RED);
            errorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            panel.add(errorLabel, BorderLayout.CENTER);
        }

        return panel;
    }
}