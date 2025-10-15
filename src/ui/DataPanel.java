package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class DataPanel extends JPanel {
    private DefaultListModel<Double> model = new DefaultListModel<>();
    private JList<Double> list = new JList<>(model);

    public DataPanel() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        JButton add = new JButton("Добавить");
        JButton gen = new JButton("Генерировать");
        JButton clear = new JButton("Очистить");
        top.add(add); top.add(gen); top.add(clear);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        add.addActionListener(this::onAdd);
        gen.addActionListener(this::onGen);
        clear.addActionListener(e -> model.clear());
    }

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
}