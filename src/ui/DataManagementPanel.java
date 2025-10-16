package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Random;

public class DataManagementPanel extends JPanel {
    private DefaultListModel<Double> model = new DefaultListModel<>();
    private JList<Double> list = new JList<>(model);

    public DataManagementPanel() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Изменено для лучшего расположения кнопок

        JButton add = new JButton("Добавить");
        JButton gen = new JButton("Генерировать");
        JButton clear = new JButton("Очистить");
        JButton importBtn = new JButton("Импорт из TXT"); // Новая кнопка
        JButton exportBtn = new JButton("Экспорт в TXT"); // Новая кнопка

        top.add(add);
        top.add(gen);
        top.add(clear);
        top.add(new JSeparator(SwingConstants.VERTICAL));
        top.add(importBtn); // Добавлена
        top.add(exportBtn); // Добавлена

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        add.addActionListener(this::onAdd);
        gen.addActionListener(this::onGen);
        clear.addActionListener(e -> model.clear());

        importBtn.addActionListener(this::onImport); // Обработчик
        exportBtn.addActionListener(this::onExport); // Обработчик
    }

    // ... (onAdd и onGen остаются без изменений)
    private void onAdd(ActionEvent e) {
        String s = JOptionPane.showInputDialog(this, "Введите число:");
        if (s == null) return;
        try {
            double v = Double.parseDouble(s.replace(',', '.'));
            model.addElement(v);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Неверный ввод числа");
        }
    }

    private void onGen(ActionEvent e) {
        String s = JOptionPane.showInputDialog(this, "Сколько сгенерировать?");
        if (s == null) return;
        try {
            int n = Integer.parseInt(s);
            Random r = new Random();
            for (int i = 0; i < n; i++) model.addElement(r.nextDouble() * 100);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Неверное количество");
        }
    }

    // Новая логика: Импорт данных
    private void onImport(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Здесь должна быть логика чтения файла и заполнения model
            JOptionPane.showMessageDialog(this, "Файл для импорта выбран: " + file.getName() +
                    "\n(Требуется реализация чтения данных).");
        }
    }

    // Новая логика: Экспорт данных
    private void onExport(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Здесь должна быть логика записи данных из model в файл
            JOptionPane.showMessageDialog(this, "Файл для экспорта выбран: " + file.getName() +
                    "\n(Требуется реализация записи данных).");
        }
    }
}