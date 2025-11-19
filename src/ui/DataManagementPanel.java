package ui;

import db.CalculationLogDAO;
import db.DBHelper;
import model.CalculationLog;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.List;

public class DataManagementPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public DataManagementPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(
                new Object[]{"ID", "Пользователь", "Тип расчёта", "Входные данные", "Результаты", "Дата"}, 0
        );

        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        loadData();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Обновить");
        JButton btnDelete = new JButton("Удалить");
        JButton btnExport = new JButton("Экспорт CSV");
        JButton btnSearch = new JButton("Поиск по типу");

        buttons.add(btnRefresh);
        buttons.add(btnDelete);
        buttons.add(btnExport);
        buttons.add(btnSearch);

        add(buttons, BorderLayout.NORTH);

        // обновление
        btnRefresh.addActionListener(e -> loadData());

        // удаление
        btnDelete.addActionListener(e -> deleteSelected());

        // экспорт
        btnExport.addActionListener(e -> exportToCSV());

        // поиск
        btnSearch.addActionListener(e -> searchByType());
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<CalculationLog> logs = CalculationLogDAO.getAll();
            for (CalculationLog log : logs) {
                model.addRow(new Object[]{
                        log.getId(),
                        getUsername(log.getUserId()),
                        log.getCalcType(),
                        log.getInputData(),
                        log.getResultData(),
                        log.getCreatedAt()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + e.getMessage());
        }
    }

    private String getUsername(int userId) {
        try {
            for (User u : DBHelper.listUsers()) {
                if (u.getId() == userId) return u.getUsername();
            }
        } catch (Exception ignored) {}
        return "unknown";
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Выберите строку!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        try {
            CalculationLogDAO.delete(id);
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка удаления: " + e.getMessage());
        }
    }

    private void exportToCSV() {
        try (FileWriter fw = new FileWriter("history_export.csv")) {
            for (int i = 0; i < model.getRowCount(); i++) {
                fw.write(
                        model.getValueAt(i, 0) + ";" +
                                model.getValueAt(i, 1) + ";" +
                                model.getValueAt(i, 2) + ";" +
                                model.getValueAt(i, 3) + ";" +
                                model.getValueAt(i, 4) + ";" +
                                model.getValueAt(i, 5) + "\n"
                );
            }
            JOptionPane.showMessageDialog(this, "Экспорт завершён!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка экспорта: " + e.getMessage());
        }
    }

    private void searchByType() {
        String type = JOptionPane.showInputDialog("Введите тип расчёта (Лаваль / Фурма):");
        if (type == null) return;

        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            if (!model.getValueAt(i, 2).toString().toLowerCase().contains(type.toLowerCase())) {
                model.removeRow(i);
            }
        }
    }
}
