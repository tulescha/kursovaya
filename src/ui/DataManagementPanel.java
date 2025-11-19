package ui;

import db.DataRecordDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class DataManagementPanel extends JPanel {

    private DefaultListModel<Double> model = new DefaultListModel<>();
    private JList<Double> list = new JList<>(model);

    public DataManagementPanel() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton add = new JButton("Добавить");
        JButton gen = new JButton("Генерировать (100)");
        JButton clear = new JButton("Удалить выбранные");
        JButton search = new JButton("Поиск");
        JButton sortAsc = new JButton("Сорт ↑");
        JButton sortDesc = new JButton("Сорт ↓");

        JButton importBtn = new JButton("Импорт");
        JButton exportBtn = new JButton("Экспорт");

        top.add(add); top.add(gen); top.add(clear);
        top.add(search); top.add(sortAsc); top.add(sortDesc);
        top.add(importBtn); top.add(exportBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        reloadFromDB();

        add.addActionListener(this::onAdd);
        gen.addActionListener(e -> generate100());
        clear.addActionListener(e -> deleteSelected());
        search.addActionListener(e -> onSearch());
        sortAsc.addActionListener(e -> loadSorted(true));
        sortDesc.addActionListener(e -> loadSorted(false));

        importBtn.addActionListener(e -> importData());
        exportBtn.addActionListener(e -> exportData());
    }

    private void reloadFromDB() {
        try {
            model.clear();
            for (double d : DataRecordDAO.getAll()) model.addElement(d);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка БД: " + ex.getMessage());
        }
    }

    private void onAdd(ActionEvent e) {
        String s = JOptionPane.showInputDialog(this, "Введите число:");
        if (s == null) return;
        try {
            double v = Double.parseDouble(s.replace(',', '.'));
            DataRecordDAO.add(v);
            reloadFromDB();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
        }
    }

    private void generate100() {
        Random r = new Random();
        try {
            for (int i = 0; i < 100; i++)
                DataRecordDAO.add(r.nextDouble() * 100);
            reloadFromDB();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка генерации: " + ex.getMessage());
        }
    }

    private void deleteSelected() {
        List<Double> sel = list.getSelectedValuesList();
        if (sel.isEmpty()) return;

        try {
            for (Double d : sel) {
                // using approximate delete (value unique enough for lab)
                // you can extend DAO to delete by exact row id
                DataRecordDAO.search(d, d).forEach(x -> {}); // placeholder
            }
            reloadFromDB();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка удаления: " + ex.getMessage());
        }
    }

    private void onSearch() {
        String minS = JOptionPane.showInputDialog(this, "Минимум:");
        String maxS = JOptionPane.showInputDialog(this, "Максимум:");

        try {
            double min = Double.parseDouble(minS);
            double max = Double.parseDouble(maxS);
            model.clear();
            for (double d : DataRecordDAO.search(min, max)) model.addElement(d);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка поиска: " + ex.getMessage());
        }
    }

    private void loadSorted(boolean asc) {
        try {
            model.clear();
            for (double d : DataRecordDAO.sorted(asc)) model.addElement(d);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка сортировки: " + e.getMessage());
        }
    }

    private void importData() {
        JFileChooser f = new JFileChooser();
        if (f.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f.getSelectedFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                DataRecordDAO.add(Double.parseDouble(line));
            }
            reloadFromDB();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка импорта: " + e.getMessage());
        }
    }

    private void exportData() {
        JFileChooser f = new JFileChooser();
        if (f.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        try (PrintWriter pw = new PrintWriter(new FileWriter(f.getSelectedFile()))) {
            for (int i = 0; i < model.size(); i++)
                pw.println(model.get(i));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка экспорта: " + e.getMessage());
        }
    }
}
